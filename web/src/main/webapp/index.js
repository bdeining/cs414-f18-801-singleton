import React from 'react';
import { render } from 'react-dom';
import { Switch, Route, BrowserRouter as Router, NavLink } from 'react-router-dom';
import Bootstrap from 'bootstrap3/dist/css/bootstrap.css';
import { Button } from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import { createStore } from 'redux';
import { connect, Provider } from 'react-redux';
import { customStore } from './stores';

import Customer from './components/Customers';
import Machine from './components/Machines';
import Exercise from './components/Exercises';
import WorkoutRoutine from './components/WorkoutRoutines';
import Trainer from './components/Trainers';

const store = createStore(customStore);

render(
    <Provider store={store}>
        <Router>
            <div className="App">
                <div className="container">
                    <LinkContainer to="/search/"><Button>Customers</Button></LinkContainer>
                    <LinkContainer to="/search/trainer"><Button>Trainers</Button></LinkContainer>
                    <LinkContainer to="/search/workouts"><Button>Workout Routine</Button></LinkContainer>
                    <LinkContainer to="/search/exercise"><Button>Exercises</Button></LinkContainer>
                    <LinkContainer to="/search/machine"><Button>Machines</Button></LinkContainer>
                    <hr/>
                </div>
                <Switch>
                    <Route exact path="/search/" component={Customer} />
                    <Route path="/search/machine" component={Machine} />
                    <Route path="/search/exercise" component={Exercise} />
                    <Route path="/search/trainer" component={Trainer} />
                    <Route path="/search/workouts" component={WorkoutRoutine} />
                </Switch>
            </div>
        </Router>
    </Provider>,
    document.getElementById('root')
);
