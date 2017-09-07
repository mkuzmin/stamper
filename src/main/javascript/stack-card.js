import React, {Component} from "react";
import Timestamp from "react-timestamp";

export class StackCard extends Component {
    constructor(props) {
        super(props);
        this.state = {stack: {}};
    }

    loadStackFromServer() {
        const self = this;
        $.ajax({
            type: "GET", url: `/api/stack/${this.props.match.params.stack_name}`, cache: false
        }).then(function (data) {
            self.setState({stack: data});
        });
    }

    componentDidMount() {
        this.loadStackFromServer();
    }

    render() {
        return (
            <div>
                <MainInfo stack={this.state.stack}/>
            </div>
        );
    }
}

class MainInfo extends Component {
    render() {
        const stack = this.props.stack;
        if (Object.keys(stack).length === 0) {
            return (<div/>);
        }
        return (
            <div>
                <h1>Stack: {stack.name}</h1>
                <DateInfoParam name="createdDate" value={stack.createdDate}/>
                <DateInfoParam name="notificationDate" value={stack.notificationDate}/>
                <DateInfoParam name="expiredDate" value={stack.expiredDate}/>
                <UserInfoParam user={stack.user}/>
            </div>);
    }
}

class DateInfoParam extends Component {
    render() {
        return (
            <p><strong>{this.props.name}:</strong> <Timestamp time={this.props.value} format='ago'/></p>
        );
    }
}

class UserInfoParam extends Component {
    render() {
        return (
            <p><strong>User:</strong> {this.props.user.name} ({this.props.user.email}) </p>
        );
    }
}