import React, { Component } from 'react';
import axios from 'axios';
import ReactTable from "react-table";
import "react-table/react-table.css";
import Select from 'react-select';
import './styles.css';

class Exercise extends React.Component {

 constructor() {
    super();
    this.state ={
        data: [],
        machineNames: [],
        show: false,
        id: '',
        selected: null
    };
  }

 clearCustomerState = () => {
    this.setState({
        commonName: '',
        machineId: '',
        machineNames: [],
        sets: '',
        durationPerSet: '',
        id: '',
        show: false
    });
 }

 showModal = () => {
    axios({
        method: 'get',
        url: '/services/rest/machinenames',
    }).then(res => {
        console.log(res);
        this.setState({
            show: true,
            machineNames: res.data
        });
    });
 }

 hideModal = () => {
    this.clearCustomerState()
 }

 deleteCustomer = () => {
    axios({
        method: 'delete',
        url: '/services/rest/exercise?id=' + this.state.id,
    }).then(res => {
        this.getCustomerData()
        this.clearCustomerState()
    });
 }

 saveModal = () => {
    if (this.state.id) {
        axios({
            method: 'put',
            url: '/services/rest/exercise',
            data: {
                "commonName": this.state.commonName,
                "id": this.state.id,
                "machineId": this.state.machineId,
                "sets": this.state.sets,
                "durationPerSet": this.state.durationPerSet
            }
        }).then(res => {
            this.getCustomerData()
        });
    } else {
        axios({
          method: 'put',
          url: '/services/rest/exercise',
          data: {
                "commonName": this.state.commonName,
                "machineId": this.state.machineId,
                "sets": this.state.sets,
                "durationPerSet": this.state.durationPerSet
          }
        }).then(res => {
            this.getCustomerData()

        });
    }

    this.clearCustomerState();
 }

 getCustomerData() {
    axios.get('/services/rest/exercise')
        .then(res => {
                this.setState({
                  data: [ ...this.state.data ]
                })

                this.setState({
                  data: res.data,
                  id: '',
                  machineNames: [],
                  show: this.state.show
                });
        });
  }

  componentDidMount() {
    this.getCustomerData()
  }

  updateInputValue = (evt) => {
    this.setState({
      [evt.target.name]: evt.target.value
    });
  }

  handleChange = (selectedOption) => {
    this.setState({ machineId: selectedOption.value });
  }

  render() {
      return (
        <div>
        <Modal show={this.state.show} handleClose={this.hideModal} handleSave={this.saveModal} handleDelete={this.deleteCustomer}>
          <p>Machine</p>
          <p>Common Name <input name='commonName' value={this.state.commonName} onChange={this.updateInputValue}/></p>
          <Select value={this.state.selectedOption} onChange={this.handleChange} options={this.state.machineNames}/>
          <p>Sets <input name='sets' value={this.state.sets} onChange={this.updateInputValue}/></p>
          <p>Duration Per Set <input name='durationPerSet' value={this.state.durationPerSet} onChange={this.updateInputValue}/></p>
          <p>ID <input name='id' value={this.state.id} readOnly /></p>
        </Modal>
        <button type='button' onClick={this.showModal}>Open</button>
          <ReactTable
            data={this.state.data}
            columns={[
              {
                Header: "Name",
                columns: [
                  {
                    Header: "Common Name",
                    accessor: "commonName"
                  },
                  {
                    Header: "Machine ID",
                    accessor: "machineId",
                  },
                  {
                    Header: "Sets",
                    accessor: "sets"
                  },
                  {
                    Header: "Duration Per Set",
                    accessor: "durationPerSet"
                  },
                  {
                    Header: "ID",
                    accessor: "id"
                  }
                ]
              }
            ]}
            defaultPageSize={10}
            className="-striped -highlight"
            getTrProps={(state, rowInfo) => {
                          if (rowInfo && rowInfo.row) {
                            return {
                              onClick: (e) => {
                                this.setState({
                                  commonName: rowInfo.row.commonName,
                                  sets: rowInfo.row.sets,
                                  durationPerSet: rowInfo.row.durationPerSet,
                                  machineId: rowInfo.row.machineId,
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

export default Exercise;
