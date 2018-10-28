package com.rpc.rsf;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.rpc.rsf.base.RpcClientManager;

/**
 * 测试rpc-zookeper连接
 *<p>Title: TestRpcClientConfig.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: </p>
 * @author sky
 * @date 2018年10月15日
 */
public class TestRpcClientConfig {
	
	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		final String node="/rpc/group";
		String nodeData="rpc-data";
		final Log log=LogFactory.getLog(TestRpcClientConfig.class);
		RpcClientManager manager = new RpcClientManager("127.0.0.1:2181");
		ZooKeeper zkClient = manager.getZookeeper();
		log.debug("local ip:"+manager.getIp());
		Stat state = zkClient.exists(node, false);
		if(state==null) {
			String res = zkClient.create(node,
					(nodeData).getBytes(), 
					ZooDefs.Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
			log.info("add node result"+res);
			state = zkClient.exists(node, true);
		}
		log.info("state :"+state);
		//String info = zkClient.create(node+"/client", ("client1").getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
//		log.info("add child node>>"+info);
		List<String> chstat = zkClient.getChildren("/rpc", new Watcher() {
			
			public void process(WatchedEvent event) {
				log.debug(node+"child node:"+event);
				
			}
		});
		List<String> cpath = manager.getChildrenPath("/test/provider/1_dev", null);
//		byte[] data = zkClient.getData(info, true, new Stat());
//		log.info("/rpc/client data:"+new String(data));
		log.info(cpath);
		log.info(chstat);
	}
}
