import React, { Component } from 'react';
import axios from 'axios';
import ReactTable from "react-table";
import "react-table/react-table.css";
import './styles.css';

class Trainer extends React.Component {

 constructor() {
    super();
    this.state ={
        data: [],
        show: false,
        id: '',
        selected: null
    };
  }

 clearCustomerState = () => {
    this.setState({
        firstName: '',
        lastName: '',
        address: '',
        phone: '',
        email: '',
        healthInsuranceProvider: '',
        workHours: '',
        id: '',
        qualifications: [],
        show: false
    });
 }

 showModal = () => {
    this.setState({
        show: true
    });
 }

 hideModal = () => {
    this.clearCustomerState()
 }

 deleteCustomer = () => {
    axios({
        method: 'delete',
        url: '/services/rest/trainer?id=' + this.state.id,
    }).then(res => {
        this.getCustomerData()
        this.clearCustomerState()
    });
 }

 saveModal = () => {
    if (this.state.id) {
        axios({
            method: 'put',
            url: '/services/rest/trainer',
            data: {
                  "address" : this.state.address,
                  "firstName" : this.state.firstName,
                  "lastName" : this.state.lastName,
                  "phone" : this.state.phone,
                  "email" : this.state.email,
                  "healthInsuranceProvider" : this.state.healthInsuranceProvider,
                  "workHours" : this.state.workHours,
                  "id" : this.state.id,
                  "qualifications" : []
            }
        }).then(res => {
            this.getCustomerData()
        });
    } else {
        axios({
          method: 'put',
          url: '/services/rest/trainer',
          data: {
                "address" : this.state.address,
                "firstName" : this.state.firstName,
                "lastName" : this.state.lastName,
                "phone" : this.state.phone,
                "email" : this.state.email,
                "healthInsuranceProvider" : this.state.healthInsuranceProvider,
                "workHours" : this.state.workHours,
                "qualifications" : []
          }
        }).then(res => {
            this.getCustomerData()

        });
    }

    this.clearCustomerState();
 }

 getCustomerData() {
    axios.get('/services/rest/trainer')
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
    this.getCustomerData()
  }

  updateInputValue = (evt) => {
    this.setState({
      [evt.target.name]: evt.target.value
    });
  }

  render() {
      return (
        <div>
        <Modal show={this.state.show} handleClose={this.hideModal} handleSave={this.saveModal} handleDelete={this.deleteCustomer}>
          <p>Customer</p>
          <p>First Name <input name='firstName' value={this.state.firstName} onChange={this.updateInputValue}/></p>
          <p>Last Name <input name='lastName' value={this.state.lastName} onChange={this.updateInputValue}/></p>
          <p>Address <input name='address' value={this.state.address} onChange={this.updateInputValue}/></p>
          <p>Phone <input name='phone' value={this.state.phone} onChange={this.updateInputValue}/></p>
          <p>Email <input name='email' value={this.state.email} onChange={this.updateInputValue}/></p>
          <p>Health Insurance Provider <input name='healthInsuranceProvider' value={this.state.healthInsuranceProvider} onChange={this.updateInputValue}/></p>
          <p>Work Hours <input name='workHours' value={this.state.workHours} onChange={this.updateInputValue}/></p>
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
                    Header: "First Name",
                    accessor: "firstName"
                  },
                  {
                    Header: "Last Name",
                    accessor: "lastName",
                  },
                  {
                    Header: "Address",
                    accessor: "address"
                  },
                  {
                    Header: "Phone",
                    accessor: "phone"
                  },
                  {
                    Header: "Email",
                    accessor: "email"
                  },
                  {
                    Header: "Work Hours",
                    accessor: "workHours"
                  },
                  {
                    Header: "Health Insurance Provider",
                    accessor: "healthInsuranceProvider"
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
                                  firstName: rowInfo.row.firstName,
                                  lastName: rowInfo.row.lastName,
                                  address: rowInfo.row.address,
                                  email: rowInfo.row.email,
                                  phone: rowInfo.row.phone,
                                  healthInsuranceProvider: rowInfo.row.healthInsuranceProvider,
                                  workHours: rowInfo.row.workHours,
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

export default Trainer;
