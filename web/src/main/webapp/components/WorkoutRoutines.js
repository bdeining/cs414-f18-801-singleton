import React, { Component } from 'react';
import axios from 'axios';
import ReactTable from "react-table";
import "react-table/react-table.css";
import Select from 'react-select';
import './styles.css';

class WorkoutRoutine extends React.Component {

 constructor() {
    super();
    this.state ={
        data: [],
        exerciseNames: [],
        show: false,
        id: '',
        selected: null
    };
  }

 clearCustomerState = () => {
    this.setState({
        name: '',
        exerciseIds: [],
        exerciseNames: [],
        id: '',
        show: false
    });
 }

 showModal = () => {
    axios({
        method: 'get',
        url: '/services/rest/exercisenames',
    }).then(res => {
        this.setState({
            show: true,
            exerciseNames: res.data
        });
    });
 }

 hideModal = () => {
    this.clearCustomerState()
 }

 deleteCustomer = () => {
    axios({
        method: 'delete',
        url: '/services/rest/routine?id=' + this.state.id,
    }).then(res => {
        this.getCustomerData()
        this.clearCustomerState()
    });
 }

 saveModal = () => {
    if (this.state.id) {
        axios({
            method: 'put',
            url: '/services/rest/routine',
            data: {
                "name": this.state.name,
                "id": this.state.id,
                "exerciseIds": this.state.exerciseIds
            }
        }).then(res => {
            this.getCustomerData()
        });
    } else {
        axios({
          method: 'put',
          url: '/services/rest/routine',
          data: {
                "name": this.state.name,
                "exerciseIds": this.state.exerciseIds
          }
        }).then(res => {
            this.getCustomerData()

        });
    }

    this.clearCustomerState();
 }

 getCustomerData() {
    axios.get('/services/rest/routine')
        .then(res => {
                this.setState({
                  data: [ ...this.state.data ]
                })

                this.setState({
                  data: res.data,
                  id: '',
                  exerciseIds: [],
                  exerciseNames: [],
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
    console.log(selectedOption)
    var names = selectedOption.map(function(item) {
      return item['value'];
    });
    console.log(names)
    this.setState({ exerciseIds: names });
  }

  render() {
      return (
        <div>
        <Modal show={this.state.show} handleClose={this.hideModal} handleSave={this.saveModal} handleDelete={this.deleteCustomer}>
          <p>Machine</p>
          <p>Name <input name='name' value={this.state.name} onChange={this.updateInputValue}/></p>
          <Select isMulti closeMenuOnSelect={false} value={this.state.selectedOption} onChange={this.handleChange} options={this.state.exerciseNames}/>
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
                    Header: "Name",
                    accessor: "name"
                  },
                  {
                    Header: "Exercise IDs",
                    accessor: "exerciseIds",
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
                                  name: rowInfo.row.name,
                                  exerciseIds: rowInfo.row.exerciseIds,
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

export default WorkoutRoutine;
