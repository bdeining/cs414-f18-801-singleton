import React, { Component } from 'react';
import axios from 'axios';
import ReactTable from "react-table";
import "react-table/react-table.css";
import './styles.css';

class Machine extends React.Component {

 constructor() {
    super();
    this.state ={
        data: [],
        show: false,
        id: '',
        selected: null
    };
  }

 clearMachineState = () => {
    this.setState({
        quantity: '',
        picture: '',
        name: '',
        id: '',
        show: false
    });
 }

 showModal = () => {
    this.setState({
        show: true
    });
 }

 hideModal = () => {
    this.clearMachineState()
 }

 deleteMachine = () => {
    axios({
        method: 'delete',
        url: '/services/rest/machine?id=' + this.state.id,
    }).then(res => {
        this.getMachineData()
        this.clearMachineState()
    });
 }

 saveModal = () => {
    if (this.state.id) {
        axios({
            method: 'put',
            url: '/services/rest/machine',
            data: {
                  "name" : this.state.name,
                  "picture" : this.state.picture,
                  "quantity" : this.state.quantity,
                  "id" : this.state.id
            }
        }).then(res => {
            this.getMachineData()
        });
    } else {
        axios({
          method: 'put',
          url: '/services/rest/machine',
          data: {
                  "name" : this.state.name,
                  "picture" : this.state.picture,
                  "quantity" : this.state.quantity
          }
        }).then(res => {
            this.getMachineData()

        });
    }

    this.clearMachineState();
 }

 getMachineData() {
    axios.get('/services/rest/machine')
        .then(res => {
                this.setState({
                  data: [ ...this.state.data ]
                })

                this.setState({
                  data: res.data,
                  id: '',
                  show: this.state.show
                });
        });
  }

  componentDidMount() {
    this.getMachineData()
  }

  updateInputValue = (evt) => {
    this.setState({
      [evt.target.name]: evt.target.value
    });
  }

  render() {
      return (
        <div>
        <Modal show={this.state.show} handleClose={this.hideModal} handleSave={this.saveModal} handleDelete={this.deleteMachine}>
          <p>Machine</p>
          <p>Name <input name='name' value={this.state.name} onChange={this.updateInputValue}/></p>
          <p>Quantity <input name='quantity' value={this.state.quantity} onChange={this.updateInputValue}/></p>
          <p>Picture <input name='picture' value={this.state.picture} onChange={this.updateInputValue}/></p>
          <p>ID <input name='id' value={this.state.id} readOnly /></p>
        </Modal>
        <button type='button' onClick={this.showModal}>Add</button>
          <ReactTable
            data={this.state.data}
            columns={[
                  {
                    Header: "Name",
                    accessor: "name"
                  },
                  {
                    Header: "Picture",
                    accessor: "picture",
                  },
                  {
                    Header: "Quantity",
                    accessor: "quantity"
                  },
                  {
                    Header: "ID",
                    accessor: "id"
                  }
            ]}
            defaultPageSize={10}
            className="-striped -highlight"
            getTrProps={(state, rowInfo) => {
                          if (rowInfo && rowInfo.row) {
                            return {
                              onClick: (e) => {
                                this.setState({
                                  name: rowInfo.row.name,
                                  picture: rowInfo.row.picture,
                                  quantity: rowInfo.row.quantity,
                                  id: rowInfo.row.id,
                                  selected: rowInfo.index
                                })
                                this.showModal()
                              },
                              style: {
                                background: rowInfo.index === this.state.selected ? '#00afec' : 'white',
                                color: rowInfo.index === this.state.selected ? 'white' : 'black'
                              }
                            }
                          }else{
                            return {}
                          }
                        }
                        }
          />
        </div>
      );
  }
}


const Modal = ({ handleClose, handleSave, handleDelete, show, children }) => {
  const showHideClassName = show ? 'modal display-block' : 'modal display-none';

  return (
    <div className={showHideClassName}>
      <section className='modal-main'>
        {children}
        <button onClick={handleClose}>
          Close
        </button>
        <button onClick={handleDelete}>
          Delete
        </button>
        <button onClick={handleSave}>
          Save
        </button>
      </section>
    </div>
  );
};

export default Machine;
