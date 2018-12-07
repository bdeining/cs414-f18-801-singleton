import React, { Component } from "react";
import axios from "axios";
import ReactTable from "react-table";
import "react-table/react-table.css";
import "./styles.css";
import Modal from "./Modal";

class Trainer extends React.Component {
  constructor() {
    super();
    this.state = {
      firstName: "",
      lastName: "",
      address: "",
      phone: "",
      email: "",
      healthInsuranceProvider: "",
      activity: "",
      workHours: "",
      data: [],
      show: false,
      qualifications: [],
      isManager: localStorage.getItem("user") === "manager",
      id: "",
      branch: localStorage.getItem("branch"),
      selected: null
    };
  }

  showAddModal = () => {
    this.setState({
      add: true
    });
    this.showModal();
  };

  clearTrainerState = () => {
    this.setState({
      firstName: "",
      lastName: "",
      address: "",
      phone: "",
      email: "",
      healthInsuranceProvider: "",
      workHours: "",
      password: "",
      id: "",
      qualifications: [],
      add: false,
      show: false
    });
  };

  showModal = () => {
    this.setState({
      show: true
    });
  };

  hideModal = () => {
    this.clearTrainerState();
  };

  deleteTrainer = () => {
    axios({
      method: "delete",
      url: "/services/rest/trainer?id=" + this.state.id
    }).then(res => {
      this.getTrainerData();
      this.clearTrainerState();
    });
  };

  validate = () => {
    return {
      address: this.state.address.length === 0,
      firstName: this.state.firstName.length === 0,
      lastName: this.state.lastName.length === 0,
      phone: this.state.phone.length === 0,
      email: this.state.email.length === 0,
      healthInsuranceProvider: this.state.healthInsuranceProvider.length === 0,
      workHours: this.state.workHours.length === 0
    };
  };

  canBeSaved = () => {
    const errors = this.validate();
    const isDisabled = Object.keys(errors).some(x => errors[x]);
    return !isDisabled;
  };

  saveModal = () => {
    if (!this.canBeSaved()) {
      return;
    }
    var qualifications = this.state.qualifications.map(function(item) {
      return item["name"];
    });

    if (this.state.id) {
      axios({
        method: "put",
        url: "/services/rest/trainer",
        data: {
          address: this.state.address,
          firstName: this.state.firstName,
          lastName: this.state.lastName,
          phone: this.state.phone,
          email: this.state.email,
          healthInsuranceProvider: this.state.healthInsuranceProvider,
          workHours: this.state.workHours,
          id: this.state.id,
          branch: this.state.branch,
          password: this.state.password,
          qualifications: qualifications
        }
      }).then(res => {
        this.getTrainerData();
      });
    } else {
      axios({
        method: "put",
        url: "/services/rest/trainer",
        data: {
          address: this.state.address,
          firstName: this.state.firstName,
          lastName: this.state.lastName,
          phone: this.state.phone,
          email: this.state.email,
          healthInsuranceProvider: this.state.healthInsuranceProvider,
          workHours: this.state.workHours,
          password: this.state.password,
          branch: this.state.branch,
          qualifications: qualifications
        }
      }).then(res => {
        this.getTrainerData();
      });
    }

    this.clearTrainerState();
  };

  getTrainerData() {
    axios
      .get("/services/rest/trainer?branch=" + this.state.branch)
      .then(res => {
        this.setState({
          data: [...this.state.data]
        });

        this.setState({
          data: res.data,
          id: "",
          show: this.state.show
        });
      });
  }

  componentDidMount() {
    this.getTrainerData();
  }

  updateInputValue = evt => {
    this.setState({
      [evt.target.name]: evt.target.value
    });
  };

  handleAddQualification = () => {
    this.setState({
      qualifications: this.state.qualifications.concat([{ name: "" }])
    });
  };

  handleQualificationNameChange = idx => evt => {
    const newQualifications = this.state.qualifications.map(
      (qualification, sidx) => {
        if (idx !== sidx) return qualification;
        return { ...qualification, name: evt.target.value };
      }
    );

    this.setState({ qualifications: newQualifications });
  };

  handleRemoveQualification = idx => () => {
    this.setState({
      qualifications: this.state.qualifications.filter(
        (s, sidx) => idx !== sidx
      )
    });
  };

  render() {
    const errors = this.validate();
    const isDisabled = Object.keys(errors).some(x => errors[x]);

    return (
      <div>
        <Modal
          show={this.state.show}
          handleClose={this.hideModal}
          handleSave={this.saveModal}
          handleDelete={this.deleteTrainer}
          add={this.state.add}
          manipulated={!this.state.isManager}
        >
          <h1>Trainer</h1>
          <div>
            <label>First Name</label>
            <input
              className={errors.firstName ? "error" : ""}
              name="firstName"
              value={this.state.firstName}
              onChange={this.updateInputValue}
              readOnly={!this.state.isManager}
            />
          </div>
          <div>
            <label>Last Name</label>
            <input
              className={errors.lastName ? "error" : ""}
              name="lastName"
              value={this.state.lastName}
              onChange={this.updateInputValue}
              readOnly={!this.state.isManager}
            />
          </div>
          <div>
            <label>Address</label>
            <input
              className={errors.address ? "error" : ""}
              name="address"
              value={this.state.address}
              onChange={this.updateInputValue}
              readOnly={!this.state.isManager}
            />
          </div>
          <div>
            <label>Phone Number</label>
            <input
              className={errors.phone ? "error" : ""}
              name="phone"
              value={this.state.phone}
              onChange={this.updateInputValue}
              readOnly={!this.state.isManager}
            />
          </div>
          <div>
            <label>Email</label>
            <input
              className={errors.email ? "error" : ""}
              name="email"
              value={this.state.email}
              onChange={this.updateInputValue}
              readOnly={!this.state.isManager}
            />
          </div>
          <div>
            <label>Health Insurance Provider</label>
            <input
              className={errors.healthInsuranceProvider ? "error" : ""}
              name="healthInsuranceProvider"
              value={this.state.healthInsuranceProvider}
              onChange={this.updateInputValue}
              readOnly={!this.state.isManager}
            />
          </div>
          <div>
            <label>Work Hours</label>
            <input
              className={errors.workHours ? "error" : ""}
              name="workHours"
              value={this.state.workHours}
              onChange={this.updateInputValue}
              readOnly={!this.state.isManager}
            />
          </div>
          {this.state.qualifications.map((qualification, idx) => (
            <div className="qualifications">
              <label>Qualification</label>
              <input
                type="text"
                placeholder={`Qualification #${idx + 1} name`}
                value={qualification.name}
                onChange={this.handleQualificationNameChange(idx)}
              />
              <button
                type="button"
                onClick={this.handleRemoveQualification(idx)}
                className="small"
              />
            </div>
          ))}
          <div>
            <button
              type="button"
              onClick={this.handleAddQualification}
              className="small"
              disabled={!this.state.isManager}
            >
              Add Qualification
            </button>
          </div>
          <div>
            <label>Password</label>
            <input
              name="password"
              value={this.state.password}
              onChange={this.updateInputValue}
              readOnly={!this.state.isManager}
            />
          </div>
          <div>
            <label>ID</label>
            <input name="id" value={this.state.id} readOnly />
          </div>
        </Modal>
        <button
          className="add"
          type="button"
          onClick={this.showAddModal}
          disabled={!this.state.isManager}
        >
          Add
        </button>
        <ReactTable
          data={this.state.data}
          columns={[
            {
              Header: "First Name",
              accessor: "firstName"
            },
            {
              Header: "Last Name",
              accessor: "lastName"
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
              Header: "Password",
              accessor: "password"
            },
            {
              Header: "ID",
              accessor: "id"
            }
          ]}
          defaultPageSize={50}
          showPaginationBottom={false}
          className="-striped -highlight"
          getTrProps={(state, rowInfo) => {
            if (rowInfo && rowInfo.row) {
              return {
                onClick: e => {
                  var qualifications = [];

                  for (var i = 0; i < rowInfo.row.qualifications.length; i++) {
                    var obj = { name: rowInfo.row.qualifications[i] };
                    qualifications.push(obj);
                  }

                  this.setState({
                    firstName: rowInfo.row.firstName,
                    lastName: rowInfo.row.lastName,
                    address: rowInfo.row.address,
                    email: rowInfo.row.email,
                    phone: rowInfo.row.phone,
                    healthInsuranceProvider:
                      rowInfo.row.healthInsuranceProvider,
                    workHours: rowInfo.row.workHours,
                    id: rowInfo.row.id,
                    qualifications: qualifications,
                    password: rowInfo.row.password,
                    selected: rowInfo.index
                  });
                  this.showModal();
                },
                style: {
                  background:
                    rowInfo.index === this.state.selected ? "#00afec" : "white",
                  color:
                    rowInfo.index === this.state.selected ? "white" : "black"
                }
              };
            } else {
              return {};
            }
          }}
        />
      </div>
    );
  }
}

export default Trainer;
