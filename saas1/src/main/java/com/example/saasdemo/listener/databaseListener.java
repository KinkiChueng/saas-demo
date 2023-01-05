//package com.example.saasdemo.listener;
//
//import com.github.shyiko.mysql.binlog.BinaryLogClient;
//import com.github.shyiko.mysql.binlog.event.*;
//
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.List;
//
//public class databaseListener {
//    void listen() {
//        BinaryLogClient client = new BinaryLogClient("host", 3333, "username", "password");
//        //设置需要读取的Binlog的文件读以及位置，否则，client会从"头"开始取Binlog并监听
////        String path = "D:\\mysql-5.7.16-winx64\\data\\mysql-bin.000001";
////        client.setBinlogFilename(path);
//        /*client.setBinlogPosition(1);*/
//        client.setServerId(1);
//        //给客户端注册监听器，实现对Binlog的监听和解析
//        //event 就是监听到的Binlog变化信息，event包含header & data 两部分
//        client.registerEventListener(event -> {
//            EventData data = event.getData();
//            Map<Long, TableInit.TableBean> tableMap = TableInit.getTableMap();
//            //动态获取当前tableId,更新map
//            if (data instanceof TableMapEventData) {
//                TableMapEventData mapEventData = (TableMapEventData) data;
//                long tableId = mapEventData.getTableId();
//                String tableName = mapEventData.getTable();
//                Long s = null;
//                if (TableInit.tableNameList.contains(tableName)) {
//                    for (Long key : TableInit.table.keySet()) {
//                        if (key != tableId && TableInit.table.get(key).getTableName().equals(tableName)) {
//                            TableInit.table.put(tableId, TableInit.table.get(key));
//                            s = key;
//                        }
//                    }
//                }
//                if (s != null) {
//                    TableInit.table.remove(s);
//                }
//            }
//            //修改监听事件
//            if (data instanceof UpdateRowsEventData) {
//                System.out.println("--------Update-----------");
//                UpdateRowsEventData eventData = (UpdateRowsEventData) data;
////                TableInit.TableBean tableBean = tableMap.get(eventData.getTableId());
////                updateBinlog(eventData, tableBean);
//                //新增监听事件
//            } else if (data instanceof WriteRowsEventData) {
//                System.out.println("--------Insert-----------");
//                WriteRowsEventData eventData = (WriteRowsEventData) data;
//                List<Serializable[]> rows = eventData.getRows();
////                saveBinlog(tableMap, rows, eventData.getTableId(), TableInit.OPERATION_TYPE_INSERT);
//                //删除的监听事件
//            } else if (data instanceof DeleteRowsEventData) {
//                System.out.println("--------Delete-----------");
//                DeleteRowsEventData eventData = (DeleteRowsEventData) data;
//                List<Serializable[]> rows = eventData.getRows();
////                saveBinlog(tableMap, rows, eventData.getTableId(), TableInit.OPERATION_TYPE_DELETE);
//            }
//        });
//        try {
//            client.connect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//}
