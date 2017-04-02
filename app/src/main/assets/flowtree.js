import React, { Component } from 'react';
import { AppRegistry,AsyncStorage, ListView, Text, View, Image, StyleSheet } from 'react-native';

//测试数据
//import testData from "./test.json"
export default class Flowtree extends Component {
  // 初始化模拟数据
  constructor(props) {
    super(props);
    const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
    //修改位置
    this.state = {
      //TODO: 注意数据进行反转
      dataSource:ds.cloneWithRows((props.flow || []).reverse())
    };
    console.log(props);
  }
  renderHeaderItem ( ) {
    return(
        <View style={styles.flowtree_header}>
          <Text>统方流程</Text>
        </View>
    );
  }

  renderRowItem( data ){
    let passRes;
    if(data.ISPASS === 1){
      passRes =(<Image style={styles.flowtree_node_icon} source={require('../res/drawable-hdpi/working_finished_check.png')}></Image>);
    }
    if(data.ISPASS === 0){
      passRes =(<Image style={styles.flowtree_node_icon} source={require('../res/drawable-hdpi/working_flow_ing.png')}></Image>);
    }
    if(data.ISPASS === -1){
      passRes =(<Image style={styles.flowtree_node_icon} source={require('../res/drawable-hdpi/working_flow_done.png')}></Image>);
    }
    return (
        <View style={styles.flowtree_node_node}>
          <View style={styles.flowtree_node_left}>
            <Image style={styles.flowtree_node_line} source={require('../res/drawable-hdpi/working_flowtree_shu.png')}></Image>
            {passRes}
            <Image style={styles.flowtree_node_line} source={require('../res/drawable-hdpi/working_flowtree_shu.png')}></Image>
          </View>
          <View style={styles.flowtree_node_info}>
            <View  style={styles.flowtree_node_content}>
              <Text>{data.info}</Text>
              {data.time&&(<Text>{data.time}</Text>)}
            </View>
          </View>
        </View>
    );
  }

  renderHeaderItem ( ) {
    return(
        <View style={styles.flowtree_header}>
          <Text>统方流程</Text>
        </View>
    );
  }

  renderFooterItem ( ){
    return(
        <Text>流程开始</Text>
    );
  }

  render() {
    return (
        <View style={styles.flowtree}>
          <ListView
              dataSource={this.state.dataSource}
              renderRow={this.renderRowItem}
              renderFooter = {this.renderFooterItem}
              renderHeader = {this.renderHeaderItem}
              />
        </View>
    );
  }
}

const styles = StyleSheet.create({
  flowtree_header: {
    height: 40,
    backgroundColor:"#FFF",
    padding:10
  },
  flowtree: {
    backgroundColor: "#F1F1F1",
    paddingBottom: 24
  },
  flowtree_node_node: {
    flex:1,
    flexDirection: "row",
    alignItems:"center",
  },
  flowtree_node_line:{
    flex:1,
    width: 20,
    minHeight : 30,
    resizeMode:"stretch"
  },
  flowtree_node_icon:{
    width: 20,
    height : 20
  },
  flowtree_node_left:{
    alignSelf:"stretch",
    flexDirection: "column",
    minHeight:80,
    width: 30,
    paddingHorizontal: 10
  },
  flowtree_node_info:{
    flex : 1,
    minHeight:80,
    padding: 10
  },
  flowtree_node_content:{
    borderWidth:1,
    borderColor:"#e1e1e1",
    backgroundColor: "#FFF",
    minHeight: 60,
    borderRadius: 6,
    padding: 4
  }
});