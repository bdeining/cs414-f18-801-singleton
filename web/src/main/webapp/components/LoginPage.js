import React, { Component } from "react";
import axios from "axios";
import Select from "react-select";

class LoginPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      email: "",
      password: ""
    };
    localStorage.removeItem("user");
    axios.get("/services/rest/branch").then(res => {
      this.setState({
        branches: res.data
      });
    });
  }

  handleSubmit = e => {
    e.preventDefault();
    axios
      .get(
        "/services/rest/login?email=" +
          this.state.email +
          "&password=" +
          this.state.password +
          "&branch=" +
          this.state.branch.value
      )
      .then(res => {
        localStorage.setItem("user", this.state.email);
        localStorage.setItem("branch", this.state.branch.value);
        this.setState({
          email: this.state.email,
          password: this.state.password,
          branch: this.state.branch.value
        });
      });
  };

  updateInputValue = evt => {
    this.setState({
      [evt.target.name]: evt.target.value
    });
  };

  handleChange = selectedOption => {
    this.setState({ branch: selectedOption });
  };

  render() {
    const isLoggedIn = localStorage.getItem("user");
    return (
      <div>
        {!isLoggedIn ? (
          <div>
            <h2>Login</h2>
            <form name="form" onSubmit={this.handleSubmit}>
              <div>
                <label>Email</label>
                <input
                  name="email"
                  value={this.state.email}
                  onChange={this.updateInputValue}
                />
              </div>
              <div>
                <label>Password</label>
                <input
                  name="password"
                  value={this.state.password}
                  onChange={this.updateInputValue}
                />
              </div>
              <Select
                value={this.state.branch}
                onChange={this.handleChange}
                options={this.state.branches}
              />
              <input type="submit" value="Submit" />
            </form>
          </div>
        ) : (
          <div className="loggedIn">
            You are logged in. Use the tabs to navigate.
          </div>
        )}
      </div>
    );
  }
}

export default LoginPage;
