package org.bboss.elasticsearchtest.crud;/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import org.frameworkset.elasticsearch.ElasticSearchException;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DocumentCRUD {
	public void testCreateIndice(){
		//创建加载配置文件的客户端工具，单实例多线程安全，第一次运行要预加载，有点慢
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/demo.xml");
		try {
			//先删除mapping
			clientUtil.dropIndice("demo");
		} catch (ElasticSearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//再创建mapping
		clientUtil.createIndiceMapping("demo",//索引表名称
				"createDemoIndice");//索引表mapping dsl脚本名称，在esmapper/demo.xml中定义createDemoIndice
	}

	public void testAddAndUpdateDocument() throws ParseException {
		//创建创建/修改/获取/删除文档的客户端对象，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		//构建一个对象，日期类型，字符串类型属性演示
		Demo demo = new Demo();
		demo.setDemoId(2l);//文档id，唯一标识，@PrimaryKey注解标示,如果demoId已经存在做修改操作，否则做添加文档操作
		demo.setAgentStarttime(new Date());
		demo.setApplicationName("blackcatdemo2");
		demo.setContentbody("this is content body2");
		demo.setName("刘德华");


		//添加或者修改文档,如果demoId已经存在做修改操作，否则做添加文档操作，返回处理结果
		String response = clientUtil.addDocument("demo",//索引表
				"demo",//索引类型
				demo);

		System.out.println("打印结果：addDocument-------------------------");
		System.out.println(response);
		//根据文档id获取文档对象，返回json报文字符串
		response = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"2");//w

		System.out.println("打印结果：getDocument-------------------------");
		System.out.println(response);
		//根据文档id获取文档对象，返回Demo对象
		demo = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"2",//文档id
				Demo.class);

		//update文档
		demo = new Demo();
		demo.setDemoId(2l);//文档id，唯一标识，@PrimaryKey注解标示,如果demoId已经存在做修改操作，否则做添加文档操作
		demo.setAgentStarttime(new Date());
		demo.setApplicationName("blackcatdemo2");
		demo.setContentbody("this is modify content body2");
		demo.setName("刘德华modify");

		//执行update操作
		response = clientUtil.addDocument("demo",//索引表
				"demo",//索引类型
				demo);
		//根据文档id获取修改后的文档对象，返回json报文字符串
		response = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"2");//w
		System.out.println("打印修改后的结果：getDocument-------------------------");
		System.out.println(response);

		//删除文档
		clientUtil.deleteDocument("demo",//索引表
				"demo",//索引类型
				"2");//文档id
		//批量删除文档
		clientUtil.deleteDocuments("demo",//索引表
				"demo",//索引类型
				"2","3");//批量删除文档ids

	}

	public void testBulkAddDocument() throws ParseException {
		//创建批量创建文档的客户端对象，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		List<Demo> demos = new ArrayList<Demo>();
		Demo demo = new Demo();//定义第一个对象
		demo.setDemoId(2l);
		demo.setAgentStarttime(new Date());
		demo.setApplicationName("blackcatdemo2");
		demo.setContentbody("this is content body2");
		demo.setName("刘德华");
		demos.add(demo);//添加第一个对象到list中

		demo = new Demo();//定义第二个对象
		demo.setDemoId(3l);
		demo.setAgentStarttime(new Date());
		demo.setApplicationName("blackcatdemo3");
		demo.setContentbody("四大天王，这种文化很好，中华人民共和国");
		demo.setName("张学友");
		demos.add(demo);//添加第二个对象到list中

		//批量添加或者修改文档，将两个对象添加到索引表demo中
		String response = clientUtil.addDocuments("demo",//索引表
				"demo",//索引类型
				demos);

		System.out.println("addDateDocument-------------------------");
		System.out.println(response);
		//获取第一个文档
		response = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"2");//w
		System.out.println("getDocument-------------------------");
		System.out.println(response);
		//获取第二个文档
		demo = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"3",//文档id
				Demo.class);
	}

	/**
	 * 批量修改文档，和批量添加文档操作一样，只是定义的文档对象标识都是刚才批量添加的文档
	 * @throws ParseException
	 */
	public void testBulkUpdateDocument() throws ParseException {
		//创建批量修改文档的客户端对象，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getRestClientUtil();
		List<Demo> demos = new ArrayList<Demo>();
		Demo demo = new Demo();
		demo.setDemoId(2l);
		demo.setAgentStarttime(new Date());
		demo.setApplicationName("blackcatdemo2");
		demo.setContentbody("bulk update content body2");
		demo.setName("刘德华bulk update ");
		demos.add(demo);

		demo = new Demo();
		demo.setDemoId(3l);
		demo.setAgentStarttime(new Date());
		demo.setApplicationName("blackcatdemo3");
		demo.setContentbody("bulk update 四大天王，这种文化很好，中华人民共和国");
		demo.setName("张学友bulk update ");
		demos.add(demo);

		//批量修改文档
		String response = clientUtil.addDocuments("demo",//索引表
				"demo",//索引类型
				demos);

		System.out.println("addDateDocument-------------------------");
		System.out.println(response);
		//获取修改后的文档json串
		response = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"2");//w
		System.out.println("getDocument-------------------------");
		System.out.println(response);
		//获取修改后的文档对象
		demo = clientUtil.getDocument("demo",//索引表
				"demo",//索引类型
				"3",//文档id
				Demo.class);
	}

	/**
	 * 检索文档
	 * @throws ParseException
	 */
	public void testSearch() throws ParseException {
		//创建加载配置文件的客户端工具，用来检索文档，单实例多线程安全
		ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil("esmapper/demo.xml");
		//设定查询条件,通过map传递变量参数值,key对于dsl中的变量名称
		//dsl中有四个变量
		//        applicationName1
		//        applicationName2
		//        startTime
		//        endTime
		Map<String,Object> params = new HashMap<String,Object>();
		//设置applicationName1和applicationName2两个变量的值
		params.put("applicationName1","blackcatdemo2");
		params.put("applicationName2","blackcatdemo3");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//设置时间范围,时间参数接受long值
		params.put("startTime",dateFormat.parse("2017-09-02 00:00:00").getTime());
		params.put("endTime",new Date().getTime());
		//执行查询，demo为索引表，_search为检索操作action
		ESDatas<Demo> esDatas =  //ESDatas包含当前检索的记录集合，最多1000条记录，由dsl中的size属性指定
				clientUtil.searchList("demo/_search",//demo为索引表，_search为检索操作action
				"searchDatas",//esmapper/demo.xml中定义的dsl语句
				params,//变量参数
				Demo.class);//返回的文档封装对象类型
		//获取结果对象列表，最多返回1000条记录
		List<Demo> demos = esDatas.getDatas();
		//获取总记录数
		long totalSize = esDatas.getTotalSize();
		System.out.println(totalSize);
	}
}