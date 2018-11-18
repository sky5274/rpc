package com.rpc.rsf.base;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import com.alibaba.fastjson.JSON;

/**
 * Rpc client config util
 *<p>Title: RpcConfig.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: </p>
 * @author sky
 * @date 2018年10月15日
 */
public class RpcConfig {
	private static  Log log=LogFactory.getLog(RpcConfig.class);
	private static RpcClientManager clientManange=null;
	public static String path;
	
	public void intiPath(String  path) {
		RpcConfig.path=path;
	}
	
	public static RpcClientManager getManager() throws IOException, KeeperException, InterruptedException {
		if(clientManange==null) {
			if(path==null) {
				clientManange=new RpcClientManager();
			}else {
				clientManange=new RpcClientManager(path);
			}
		}
		return clientManange;
	}
	
	public static RpcClientManager getManager(String path) throws IOException, KeeperException, InterruptedException {
		if(clientManange==null) {
			clientManange=new RpcClientManager(path);
		}
		return clientManange;
	}
	
	/**
	 * 初始化注册节点
	* <p>Title: regist</p>
	* <p>Description: </p>
	* @param url
	 * @param className 
	 * @throws Exception 
	 */
	public static void regist(String url, String className,int port) throws Exception {
		String[] uarray = url.substring(1).split("/");
		log.info("node url:"+url+" --"+JSON.toJSONString(uarray));
		RpcClientManager manager = getManager();
		int rootIndex = getRootPathIndex(manager,uarray);
		for(int i=rootIndex;i<uarray.length;i++) {
			String upath = getPath(uarray, i);
			String u_result = manager.create(upath, upath);
			log.debug("rpc config add node path:"+u_result);
			if(u_result==null && i!=uarray.length-1) {
				throw new InterruptedException("zookeeper create node error");
			}
		}
		//添加子节点
		String nodeData=JSON.toJSONString(new nodeData(RpcClientManager.getIp(), port, className));
		String implPath=manager.create(url+"/"+className, nodeData);
		String tempPath=manager.createTemp(url+"/"+className+"/"+getServerUrlIndex(implPath), nodeData);
		log.debug("service defind url: "+tempPath);
	}
	
	public static List<String> getInitServerUrl(String url){
		List<String> list = new LinkedList<String>();
			try {
				list = getManager().getChildrenPath(url, null);
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return list;
	}
	
	public static long getServerUrlIndex(String url) {
		Set<Long> list=new HashSet<Long>();
		try {
			List<String> nodes = getManager().getChildrenNode(url, null);
			for(String s:nodes) {
				list.add(Long.valueOf(s));
			}
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getNullIndex(list);
	}
	
	/**
	 * 获取节点的空位序列号
	* <p>Title: getNullIndex</p>
	* <p>Description: </p>
	* @param list
	* @return
	 */
	public static long getNullIndex(Set<Long> list) {
		if(list==null || list.isEmpty()) {
			return 0;
		}
		long index=0;
		for(int i=0;i<list.size();i++) {
			if(list.contains(index)) {
				index++;
			}else {
				break;
			}
		}
		return index;
	}
	
	/**
	 * 获取节点序号
	* <p>Title: getRootPathIndex</p>
	* <p>Description: </p>
	* @param manager
	* @param ulist
	* @return
	* @throws KeeperException
	* @throws InterruptedException
	* @throws IOException
	 */
	private static int getRootPathIndex(RpcClientManager manager, String [] ulist) throws KeeperException, InterruptedException, IOException {
		for(int i=0;i<ulist.length;i++) {
			if(manager.exists(getPath(ulist,i))==null){
				return i;
			}
		}
		return ulist.length>0?0: ulist.length-1;
	}
	
	private static String getPath(String [] ulist,int i) {
		if(i<0) {
			return null;
		}
		String path="/"+ulist[0];
		for(int j=1;j<=i;j++) {
			path+="/"+ulist[j];
		}
		return path;
	}
	
	public static class nodeData {
		private String ip;
		private int port;
		private String className;
		public nodeData() {}
		public nodeData(String ip,int port,String className) throws ClassNotFoundException {
			this.ip=ip;
			this.port=port;
			this.setClassName(className);;
		}
		
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) throws ClassNotFoundException {
			this.className = className;
		}
		
	}

	/**
	 * 随机获取接口对应得服务端信息
	* <p>Title: getRandomServer</p>
	* <p>Description: </p>
	* @param name
	* @return
	 */
	public static nodeData getRandomServer(String url) {
		log.debug("rpc config get server request mapper:"+url);
		List<String> list=new LinkedList<String>();
		try {
			list = clientManange.getChildrenPath(url, null);
		} catch (KeeperException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(!list.isEmpty()) {
			Random random = new Random(list.size());
			String upath = list.get((int) Math.round(random.nextDouble())-1);
			try {
				String data = new String(getManager().getData(upath, new Watcher() {
					
					public void process(WatchedEvent event) {
						log.debug("get new rpc client watch"+event.getPath());
						log.debug("get new rpc client watch"+event.getState());
						log.debug("get new rpc client watch"+event.getType());
						log.debug("get new rpc client watch"+event.getWrapper());
					}
				}, new Stat()));
				log.debug(String.format("rpc config get random client node:[path:%s,data:%s]",upath,data));
				return JSON.parseObject(data,nodeData.class);
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}


