import React, { Component } from "react";
import axios from "axios";
import ReactTable from "react-table";
import "react-table/react-table.css";
import Select from "react-select";
import "./styles.css";
import Modal from "./Modal";

class WorkoutRoutine extends React.Component {
  constructor() {
    super();
    this.state = {
      data: [],
      exerciseIds: [],
      exerciseNames: [],
      show: false,
      add: false,
      id: "",
      isManager: localStorage.getItem("user") === "manager",
      branch: localStorage.getItem("branch"),
      selected: null
    };

    axios({
      method: "get",
      url: "/services/rest/exercisenames?branch=" + this.state.branch
    }).then(res => {
      this.setState({
        exerciseNames: res.data
      });
    });
  }

  showAddModal = () => {
    this.setState({
      add: true
    });
    this.showModal();
  };

  clearWorkoutRoutineState = () => {
    this.setState({
      name: "",
      exerciseIds: [],
      id: "",
      add: false,
      show: false
    });
  };

  showModal = () => {
    axios({
      method: "get",
      url: "/services/rest/exercisenames?branch=" + this.state.branch
    }).then(res => {
      this.setState({
        show: true,
        exerciseNames: res.data
      });
    });
  };

  hideModal = () => {
    this.clearWorkoutRoutineState();
  };

  deleteWorkoutRoutine = () => {
    axios({
      method: "delete",
      url: "/services/rest/routine?id=" + this.state.id
    }).then(res => {
      this.getWorkoutRoutineData();
      this.clearWorkoutRoutineState();
    });
  };

  saveModal = () => {
    var exerciseIdList = this.state.exerciseIds.map(function(item) {
      return item["value"];
    });

    if (this.state.id) {
      axios({
        method: "put",
        url: "/services/rest/routine",
        data: {
          name: this.state.name,
          id: this.state.id,
          branch: this.state.branch,
          exerciseIds: exerciseIdList
        }
      }).then(res => {
        this.getWorkoutRoutineData();
      });
    } else {
      axios({
        method: "put",
        url: "/services/rest/routine",
        data: {
          name: this.state.name,
          branch: this.state.branch,
          exerciseIds: exerciseIdList
        }
      }).then(res => {
        this.getWorkoutRoutineData();
      });
    }

    this.clearWorkoutRoutineState();
  };

  getWorkoutRoutineData() {
    axios.get("/services/rest/routine?branch=" + this.state.branch).then(res => {
      this.setState({
        data: [...this.state.data]
      });

      this.setState({
        data: res.data,
        id: "",
        exerciseIds: [],
        show: this.state.show
      });
    });
  }

  componentDidMount() {
    this.getWorkoutRoutineData();
  }

  updateInputValue = evt => {
    this.setState({
      [evt.target.name]: evt.target.value
    });
  };

  handleChange = selectedOption => {
    this.setState({ exerciseIds: selectedOption });
  };

  render() {
    return (
      <div>
        <Modal
          show={this.state.show}
          handleClose={this.hideModal}
          handleSave={this.saveModal}
          handleDelete={this.deleteWorkoutRoutine}
          add={this.state.add}
          manipulated={this.state.isManager}
        >
          <h1>Workout Routine</h1>
          <div>
            <label>Name</label>
            <input
              name="name"
              value={this.state.name}
              onChange={this.updateInputValue}
              readOnly={this.state.isManager}
            />
          </div>
          <div>
            <label>Exercise Names</label>
            <div className="react-select-container">
              <Select
                isMulti
                closeMenuOnSelect={false}
                value={this.state.exerciseIds}
                onChange={this.handleChange}
                options={this.state.exerciseNames}
                isDisabled={this.state.isManager}
              />
            </div>
          </div>
          <div>
            <label>ID</label>
            <input name="id" value={this.state.id} readOnly />
          </div>
        </Modal>
        <button
          type="button"
          onClick={this.showAddModal}
          disabled={this.state.isManager}
        >
          Add
        </button>
        <ReactTable
          data={this.state.data}
          columns={[
            {
              Header: "Name",
              accessor: "name"
            },
            {
              Header: "Exercise IDs",
              accessor: "exerciseIds",
              Cell: row => <span>{row.value.join(", ")}</span>
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
              var exerciseNameList = [];
              for (var i = 0; i < this.state.exerciseNames.length; i++) {
                for (var c = 0; c < rowInfo.row.exerciseIds.length; c++) {
                  if (
                    this.state.exerciseNames[i].value ===
                    rowInfo.row.exerciseIds[c]
                  ) {
                    exerciseNameList.push(this.state.exerciseNames[i]);
                  }
                }
              }

              return {
                onClick: e => {
                  this.setState({
                    name: rowInfo.row.name,
                    exerciseIds: exerciseNameList,
                    id: rowInfo.row.id,
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

export default WorkoutRoutine;
