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
        qualifications: [],
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
    var qualifications = this.state.qualifications.map(function(item) {
      return item['name'];
    });

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
                  "qualifications" : qualifications
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
                "qualifications" : qualifications
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

  handleAddQualification = () => {
    this.setState({ qualifications: this.state.qualifications.concat([{ name: '' }]) });
  }

  handleQualificationNameChange = (idx) => (evt) => {
    const newQualifications = this.state.qualifications.map((qualification, sidx) => {
      if (idx !== sidx) return qualification;
      return { ...qualification, name: evt.target.value };
    });

    this.setState({ qualifications: newQualifications });
  }

  handleRemoveQualification = (idx) => () => {
    this.setState({ qualifications: this.state.qualifications.filter((s, sidx) => idx !== sidx) });
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
            {this.state.qualifications.map((qualification, idx) => (
              <div className="qualifications">
                <input
                  type="text"
                  placeholder={`Qualification #${idx + 1} name`}
                  value={qualification.name}
                  onChange={this.handleQualificationNameChange(idx)}
                />
                <button type="button" onClick={this.handleRemoveQualification(idx)} className="small">-</button>
              </div>
            ))}
            <button type="button" onClick={this.handleAddQualification} className="small">Add Qualification</button>

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
                    Header: "Qualifications",
                    accessor: "qualifications"
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

                                  var qualifications = [];

                                  for (var i = 0; i < rowInfo.row.qualifications.length; i++) {
                                    var obj = {"name" : rowInfo.row.qualifications[i]};
                                    qualifications.push(obj);
                                  }

                                this.setState({
                                  firstName: rowInfo.row.firstName,
                                  lastName: rowInfo.row.lastName,
                                  address: rowInfo.row.address,
                                  email: rowInfo.row.email,
                                  phone: rowInfo.row.phone,
                                  healthInsuranceProvider: rowInfo.row.healthInsuranceProvider,
                                  workHours: rowInfo.row.workHours,
                                  id: rowInfo.row.id,
                                  qualifications: qualifications,
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
