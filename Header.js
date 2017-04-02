import React, { Component, PropTypes } from 'react';
import {
    View,
    Image,
    Text,
    StyleSheet
    } from 'react-native';
//import FirstPageComponent from './FirstPageComponent';


export default class Header extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }
    render() {
        return(
            <View style={styles.header}>
                <Image style={styles.img} source={require('./app/src/main/res/drawable-hdpi/back.png')}/>
                <Text style={styles.headerText} onPress={this.props.onBack}>{this.props.name.toString()}</Text>
            </View>
        );

    }
}



const styles = StyleSheet.create({
    header:{
        flex: 0.8,
        flexDirection: 'row',
        alignItems:"center",
        justifyContent:'flex-start',
        borderBottomWidth:1,
        borderBottomColor:'#ccc',
        backgroundColor:'#008CEE',
        paddingLeft:10,
    },
    img:{
        width:20,
        height:20
    },
    headerText:{
        fontSize:16

    }
});