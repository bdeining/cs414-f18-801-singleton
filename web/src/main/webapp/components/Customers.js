import React, { Component } from 'react';
import axios from 'axios';
import ReactTable from "react-table";
import "react-table/react-table.css";
import './styles.css';

class Home extends React.Component {

 constructor() {
    super();
            this.state ={
                data: [],
                show: false,
                id: '',
                selected: null
              };
  }

    showModal = () => {
      this.setState({
      show: true,

        });
    }

    hideModal = () => {
      this.setState({ show: false });
    }

    deleteCustomer = () => {
      axios({
                  method: 'delete',
                  url: '/services/rest/customer?id=' + this.state.id,
                             success() {
                               this.getCustomerData()
                             }
                          }).then(res => {
                                                   this.getCustomerData()
                                                   this.setState({ show: false })
                                              });

    }

    saveModal = () => {
        console.log(this.state);
        if (this.state.id) {
          axios({
            method: 'put',
            url: '/services/rest/customer',
            data: {
                  "address" : this.state.address,
                  "firstName" : this.state.firstName,
                  "lastName" : this.state.lastName,
                  "phone" : this.state.phone,
                  "email" : this.state.email,
                  "healthInsuranceProvider" : this.state.healthInsuranceProvider,
                  "activity" : this.state.activity,
                  "id" : this.state.id,
                  "workoutRoutineIds" : []
            }}).then(res => {
                         this.getCustomerData()
                    });
        } else {

        axios({
          method: 'put',
          url: '/services/rest/customer',
          data: {
                "address" : this.state.address,
                "firstName" : this.state.firstName,
                "lastName" : this.state.lastName,
                "phone" : this.state.phone,
                "email" : this.state.email,
                "healthInsuranceProvider" : this.state.healthInsuranceProvider,
                "activity" : this.state.activity,
                "workoutRoutineIds" : []
          }

        }).then(res => {
                                 this.getCustomerData()
                            });
        }
      this.setState({ show: false });
    }

  getCustomerData() {
      axios.get('/services/rest/customer')
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
  console.log('update' + evt.target.name + ' ' + evt.target.value);
  console.log(evt);
    this.setState({
      [evt.target.name]: evt.target.value
    });
  }

  render() {
      //const { data } = this.state;
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
          <p>Activity <input name='activity' value={this.state.activity} onChange={this.updateInputValue}/></p>
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
                    Header: "Activity",
                    accessor: "activity"
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
                                console.log(e);
                                console.log(rowInfo);
                                console.log(this.state);
                                this.setState({
                                  firstName: rowInfo.row.firstName,
                                  lastName: rowInfo.row.lastName,
                                  address: rowInfo.row.address,
                                  email: rowInfo.row.email,
                                  phone: rowInfo.row.phone,
                                  healthInsuranceProvider: rowInfo.row.healthInsuranceProvider,
                                  activity: rowInfo.row.activity,
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

export default Home;
