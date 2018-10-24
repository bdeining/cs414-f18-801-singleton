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
                show: false
              };
  }

    showModal = () => {
      this.setState({ show: true });
    }

    hideModal = () => {
      this.setState({ show: false });
    }

    saveModal = () => {
/*        axios.put('/services/rest/customer', {
            data: '{"address" : "2516 S Scap", "firstName" : "Ben", "lastName" : "Deininger", "phone" : "520-123-1234", "email" : "test@example.com", "healthInsuranceProvider" : "health insurance", "activity" : "ACTIVE", "workoutRoutineIds" : ["7a3d4681-0640-4741-bc94-d4d445a0c6ee"] }'
        })
            .then(function (response) {
                console.log(response);
            }.bind(this))
            .catch(function (error) {
                console.log(error)
            });*/
console.log(this.state);
axios({
  method: 'put',
  url: '/services/rest/customer',
  data: {
		"address" : "2516 S Scap",
    	"firstName" : "Ben",
    	"lastName" : "Deininger",
    	"phone" : "520-123-1234",
    	"email" : "test@example.com",
    	"healthInsuranceProvider" : "health insurance",
    	"activity" : "ACTIVE",
        "workoutRoutineIds" : ["7a3d4681-0640-4741-bc94-d4d445a0c6ee"]
  }
});
      this.setState({ show: false });
    }

  componentDidMount() {
    axios.get('/services/rest/customer')
      .then(res => {
              this.setState({
                data: res.data,
                show: this.state.show
              });
      });
  }

  updateInputValue = (evt) => {
  console.log('update' + evt.target.name + ' ' + evt.target.value);
  console.log(evt);
    this.setState({
      [evt.target.name]: evt.target.value
    });
  }

  render() {
      const { data } = this.state;
      return (
        <div>
        <Modal show={this.state.show} handleClose={this.hideModal} handleSave={this.saveModal}>
          <p>Customer</p>
          <p>First Name <input name='firstName' value={this.state.firstName} onChange={this.updateInputValue}/></p>
          <p>Last Name <input name='lastName' value={this.state.lastName} onChange={this.updateInputValue}/></p>
          <p>Address <input name='address' value={this.state.phone} onChange={this.updateInputValue}/></p>
          <p>Phone <input name='phone' value={this.state.email} onChange={this.updateInputValue}/></p>
          <p>Email <input name='email' value={this.state.lastName} onChange={this.updateInputValue}/></p>
          <p>Health Insurance Provider <input name='healthInsuranceProvider' value={this.state.healthInsuranceProvider} onChange={this.updateInputValue}/></p>
          <p>Activity <input name='activity' value={this.state.activity} onChange={this.updateInputValue}/></p>
        </Modal>
        <button type='button' onClick={this.showModal}>Open</button>
          <ReactTable
            data={data}
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
                ]
              }
            ]}
            defaultPageSize={10}
            className="-striped -highlight"
          />
        </div>
      );
  }
}


const Modal = ({ handleClose, handleSave, show, children }) => {
  const showHideClassName = show ? 'modal display-block' : 'modal display-none';

  return (
    <div className={showHideClassName}>
      <section className='modal-main'>
        {children}
        <button onClick={handleClose}>
          Close
        </button>
        <button onClick={handleSave}>
          Save
        </button>
      </section>
    </div>
  );
};

export default Home;
