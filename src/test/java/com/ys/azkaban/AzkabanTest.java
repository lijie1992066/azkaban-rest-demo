package com.ys.azkaban;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ys.azkaban.utils.SSLUtil;

public class AzkabanTest {

	// azkaban api addr : http://azkaban.github.io/azkaban/docs/latest/#ajax-api

//	private static String API = "https://lijie:8443";
//	private static String API = "https://ysbd-h07:8443";
	private static String API = "https://10.0.0.87:8443";

	RestTemplate restTemplate = null;

	@Before
	public void beforeTest() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(2000);
		requestFactory.setReadTimeout(2000);
		restTemplate = new RestTemplate(requestFactory);
	}

	/**
	 * 登录测试 登录调度系统
	 * 
	 * @throws Exception
	 */
	@Test
	public void loginTest() throws Exception {

		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<String, String>();
		linkedMultiValueMap.add("action", "login");
		linkedMultiValueMap.add("username", "azkaban");
		linkedMultiValueMap.add("password", "azkaban");

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(linkedMultiValueMap, hs);
		String postForObject = restTemplate.postForObject(API, httpEntity, String.class);
		System.out.println(postForObject);
	}

	/**
	 * 创建任务测试 创建一个project
	 * 
	 * @throws Exception
	 */
	@Test
	public void createProTest() throws Exception {
		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<String, String>();
		linkedMultiValueMap.add("session.id", "fa47065d-4f00-46ea-bc82-c766a6262886");
		linkedMultiValueMap.add("action", "create");
		linkedMultiValueMap.add("name", "ystest0101");
		linkedMultiValueMap.add("description", "ystest01 description");

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(linkedMultiValueMap, hs);
		String postForObject = restTemplate.postForObject(API + "/manager", httpEntity, String.class);
		System.out.println(postForObject);

	}

	/**
	 * 删除任务测试 删除一个project
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteProTest() throws Exception {

		SSLUtil.turnOffSslChecking();

		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		hs.add("Accept", "text/plain;charset=utf-8");

		Map<String, String> map = new HashMap<>();

		map.put("id", "64d6ce03-3ffa-44b5-9eaf-fad28d09b3e9");
		map.put("project", "ystest0101");

		ResponseEntity<String> exchange = restTemplate.exchange(
				API + "/manager?session.id={id}&delete=true&project={project}", HttpMethod.GET,
				new HttpEntity<String>(hs), String.class, map);

		System.out.println(exchange.getBody());
	}

	/**
	 * 上传zip 上传依赖文件 zip包
	 * 
	 * @throws Exception
	 */
	@Test
	public void uploadZip() throws Exception {

		SSLUtil.turnOffSslChecking();
		FileSystemResource resource = new FileSystemResource(new File("C:\\Users\\Administrator\\Desktop\\ystest.zip"));
		LinkedMultiValueMap<String, Object> linkedMultiValueMap = new LinkedMultiValueMap<String, Object>();
		linkedMultiValueMap.add("session.id", "64d6ce03-3ffa-44b5-9eaf-fad28d09b3e9");
		linkedMultiValueMap.add("ajax", "upload");
		linkedMultiValueMap.add("project", "ystest01");
		linkedMultiValueMap.add("file", resource);
		String postForObject = restTemplate.postForObject(API + "/manager", linkedMultiValueMap, String.class);
		System.out.println(postForObject);
	}

	/**
	 * Fetch Flows of a Project 获取一个project的流ID
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void fetchFlowAProTest() throws KeyManagementException, NoSuchAlgorithmException {

		SSLUtil.turnOffSslChecking();

		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		hs.add("Accept", "text/plain;charset=utf-8");

		Map<String, String> map = new HashMap<>();

		map.put("id", "64d6ce03-3ffa-44b5-9eaf-fad28d09b3e9");
		map.put("project", "lijie_demo");

		ResponseEntity<String> exchange = restTemplate.exchange(
				API + "/manager?session.id={id}&ajax=fetchprojectflows&project={project}", HttpMethod.GET,
				new HttpEntity<String>(hs), String.class, map);

		System.out.println(exchange.getBody());

	}

	/**
	 * Fetch Jobs of a Flow 获取一个job的流结构 依赖关系
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void fetchFlowTest() throws KeyManagementException, NoSuchAlgorithmException {
		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		hs.add("Accept", "text/plain;charset=utf-8");

		Map<String, String> map = new HashMap<>();

		map.put("id", "64d6ce03-3ffa-44b5-9eaf-fad28d09b3e9");
		map.put("project", "lijie_demo");
		map.put("flow", "end");

		ResponseEntity<String> exchange = restTemplate.exchange(
				API + "/manager?session.id={id}&ajax=fetchflowgraph&project={project}&flow={flow}", HttpMethod.GET,
				new HttpEntity<String>(hs), String.class, map);

		System.out.println(exchange.getBody());

	}

	/**
	 * Fetch Executions of a Flow 获取执行的project 列表
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void fetchEXEFlowTest() throws KeyManagementException, NoSuchAlgorithmException {
		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		hs.add("Accept", "text/plain;charset=utf-8");

		Map<String, String> map = new HashMap<>();

		map.put("id", "64d6ce03-3ffa-44b5-9eaf-fad28d09b3e9");
		map.put("project", "ystest01");
		map.put("flow", "end");
		map.put("start", "1");
		map.put("length", "30");

		ResponseEntity<String> exchange = restTemplate.exchange(
				API + "/manager?session.id={id}&ajax=fetchFlowExecutions&project={project}&flow={flow}&start={start}&length={length}",
				HttpMethod.GET, new HttpEntity<String>(hs), String.class, map);

		System.out.println(exchange.getBody());

	}

	/**
	 * Fetch Running Executions of a Flow 获取正在执行的流id
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void fetchRunningEXEFlowTest() throws KeyManagementException, NoSuchAlgorithmException {
		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		hs.add("Accept", "text/plain;charset=utf-8");

		Map<String, String> map = new HashMap<>();
		map.put("id", "46e144bf-0be8-419e-a200-c83a5461b894");
		map.put("project", "ystest01");
		map.put("flow", "end");
		ResponseEntity<String> exchange = restTemplate.exchange(
				API + "/executor?session.id={id}&ajax=getRunning&project={project}&flow={flow}", HttpMethod.GET,
				new HttpEntity<String>(hs), String.class, map);
		System.out.println(exchange.getBody());
	}

	/**
	 * Execute a Flow 执行一个流 还有很多其他参数 具体参考api
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void fetchEXEaFlowTest() throws KeyManagementException, NoSuchAlgorithmException {
		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		hs.add("Accept", "text/plain;charset=utf-8");

		Map<String, String> map = new HashMap<>();
		map.put("id", "46e144bf-0be8-419e-a200-c83a5461b894");
		map.put("project", "ystest01");
		map.put("flow", "end");
		ResponseEntity<String> exchange = restTemplate.exchange(
				API + "/executor?session.id={id}&ajax=executeFlow&project={project}&flow={flow}", HttpMethod.GET,
				new HttpEntity<String>(hs), String.class, map);
		System.out.println(exchange.getBody());
	}

	/**
	 * Cancel a Flow Execution 中断一个执行流
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void cancelEXEaFlowTest() throws KeyManagementException, NoSuchAlgorithmException {
		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		hs.add("Accept", "text/plain;charset=utf-8");

		Map<String, String> map = new HashMap<>();
		map.put("id", "46e144bf-0be8-419e-a200-c83a5461b894");
		map.put("execid", "10");
		ResponseEntity<String> exchange = restTemplate.exchange(
				API + "/executor?session.id={id}&ajax=cancelFlow&execid={execid}", HttpMethod.GET,
				new HttpEntity<String>(hs), String.class, map);
		System.out.println(exchange.getBody());
	}

	/**
	 * Schedule a period-based Flow 创建调度任务
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void scheduleEXEaFlowTest() throws KeyManagementException, NoSuchAlgorithmException {
		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<String, String>();
		linkedMultiValueMap.add("session.id", "fa47065d-4f00-46ea-bc82-c766a6262886");
		linkedMultiValueMap.add("ajax", "scheduleFlow");
		linkedMultiValueMap.add("projectName", "ystest01");
		linkedMultiValueMap.add("projectId", "14");

		linkedMultiValueMap.add("flow", "end");
		linkedMultiValueMap.add("scheduleTime", "10,28,am,EDT");
		linkedMultiValueMap.add("scheduleDate", "05/16/2017");
		linkedMultiValueMap.add("flowName", "ystest01 description");

		// 是否循环
		linkedMultiValueMap.add("is_recurring", "on");

		// 循环周期 天 年 月等
		// M Months
		// w Weeks
		// d Days
		// h Hours
		// m Minutes
		// s Seconds
		linkedMultiValueMap.add("period", "d");

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(linkedMultiValueMap, hs);
		String postForObject = restTemplate.postForObject(API + "/schedule", httpEntity, String.class);
		System.out.println(postForObject);
	}

	/**
	 * Flexible scheduling using Cron 通过cron表达式调度执行 创建调度任务
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void scheduleByCronEXEaFlowTest() throws KeyManagementException, NoSuchAlgorithmException {
		// TODO 执行有问题
		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<String, String>();
		linkedMultiValueMap.add("session.id", "ffad7355-4427-4770-9c14-3d19736fa73a");
		linkedMultiValueMap.add("ajax", "scheduleCronFlow");
		linkedMultiValueMap.add("projectName", "ystest01");
		linkedMultiValueMap.add("cronExpression", "* * * * *");
		linkedMultiValueMap.add("flowName", "end");

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(linkedMultiValueMap, hs);
		String postForObject = restTemplate.postForObject(API + "/schedule", httpEntity, String.class);
		System.out.println(postForObject);
	}

	/**
	 * Fetch a Schedule 获取一个调度器job的信息 根据project的id 和 flowId
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void fetchScheduleInfoTest() throws KeyManagementException, NoSuchAlgorithmException {
		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		hs.add("Accept", "text/plain;charset=utf-8");

		Map<String, String> map = new HashMap<>();
		map.put("id", "46e144bf-0be8-419e-a200-c83a5461b894");
		map.put("projectId", "14");
		map.put("flowId", "end");
		ResponseEntity<String> exchange = restTemplate.exchange(
				API + "/schedule?session.id={id}&ajax=fetchSchedule&projectId={projectId}&flowId={flowId}",
				HttpMethod.GET, new HttpEntity<String>(hs), String.class, map);
		System.out.println(exchange.getBody());
	}

	/**
	 * Unschedule a Flow 取消一个流的调度
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void unscheduleFlowTest() throws KeyManagementException, NoSuchAlgorithmException {
		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<String, String>();
		linkedMultiValueMap.add("session.id", "ffad7355-4427-4770-9c14-3d19736fa73a");
		linkedMultiValueMap.add("action", "removeSched");
		linkedMultiValueMap.add("scheduleId", "4");

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(linkedMultiValueMap, hs);
		String postForObject = restTemplate.postForObject(API + "/schedule", httpEntity, String.class);
		System.out.println(postForObject);
	}

	/**
	 * Set a SLA 设置调度任务 执行的时候 或者执行成功失败等等的规则匹配 发邮件或者...
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void setScheduleFlowSLATest() throws KeyManagementException, NoSuchAlgorithmException {
		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<String, String>();
		linkedMultiValueMap.add("session.id", "ffad7355-4427-4770-9c14-3d19736fa73a");
		linkedMultiValueMap.add("ajax", "setSla");
		linkedMultiValueMap.add("scheduleId", "6");
		linkedMultiValueMap.add("slaEmails", "779261177@qq.com");
		linkedMultiValueMap.add("settings[0]", "begin,SUCCESS,5:00,true,false");
		linkedMultiValueMap.add("settings[1]", "exe,SUCCESS,5:00,true,false");
		linkedMultiValueMap.add("settings[2]", "end,SUCCESS,5:00,true,false");
		// linkedMultiValueMap.add("settings[3]",
		// "xxx,SUCCESS,5:00,true,false");

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(linkedMultiValueMap, hs);
		String postForObject = restTemplate.postForObject(API + "/schedule", httpEntity, String.class);
		System.out.println(postForObject);
	}

	/**
	 * Fetch a SLA 获取调度的规则配置
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void fetchScheduleSLAInfoTest() throws KeyManagementException, NoSuchAlgorithmException {

		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		hs.add("Accept", "text/plain;charset=utf-8");

		Map<String, String> map = new HashMap<>();
		map.put("id", "c4adf192-dcf4-4e05-bd08-f6989dc544a7");
		map.put("scheduleId", "6");
		ResponseEntity<String> exchange = restTemplate.exchange(
				API + "/schedule?session.id={id}&ajax=slaInfo&scheduleId={scheduleId}", HttpMethod.GET,
				new HttpEntity<String>(hs), String.class, map);
		System.out.println(exchange.getBody());
	}

	/**
	 * Pause a Flow Execution 暂停一个执行流
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void pauseScheduleTest() throws KeyManagementException, NoSuchAlgorithmException {

		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		hs.add("Accept", "text/plain;charset=utf-8");

		Map<String, String> map = new HashMap<>();
		map.put("id", "c4adf192-dcf4-4e05-bd08-f6989dc544a7");
		map.put("execid", "12");
		ResponseEntity<String> exchange = restTemplate.exchange(
				API + "/executor?session.id={id}&ajax=pauseFlow&execid={execid}", HttpMethod.GET,
				new HttpEntity<String>(hs), String.class, map);
		System.out.println(exchange.getBody());
	}

	/**
	 * Resume a Flow Execution 重新执行一个执行流
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void resumeScheduleTest() throws KeyManagementException, NoSuchAlgorithmException {

		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		hs.add("Accept", "text/plain;charset=utf-8");

		Map<String, String> map = new HashMap<>();
		map.put("id", "c4adf192-dcf4-4e05-bd08-f6989dc544a7");
		map.put("execid", "11");
		ResponseEntity<String> exchange = restTemplate.exchange(
				API + "/executor?session.id={id}&ajax=resumeFlow&execid={execid}", HttpMethod.GET,
				new HttpEntity<String>(hs), String.class, map);
		System.out.println(exchange.getBody());
	}

	/**
	 * Fetch a Flow Execution 获取一个执行流的详细信息 这个流的每个节点的信息 成功或者失败等等
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void fetchFlowInfoTest() throws KeyManagementException, NoSuchAlgorithmException {

		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		hs.add("Accept", "text/plain;charset=utf-8");

		Map<String, String> map = new HashMap<>();
		map.put("id", "c4adf192-dcf4-4e05-bd08-f6989dc544a7");
		map.put("execid", "11");
		ResponseEntity<String> exchange = restTemplate.exchange(
				API + "/executor?session.id={id}&ajax=fetchexecflow&execid={execid}", HttpMethod.GET,
				new HttpEntity<String>(hs), String.class, map);
		System.out.println(exchange.getBody());
	}

	/**
	 * Fetch Execution Job Logs 获取一个执行流的日志
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void fetchFlowLogTest() throws KeyManagementException, NoSuchAlgorithmException {

		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		hs.add("Accept", "text/plain;charset=utf-8");

		Map<String, String> map = new HashMap<>();
		map.put("id", "c4adf192-dcf4-4e05-bd08-f6989dc544a7");
		map.put("execid", "11");
		map.put("jobId", "exe");
		map.put("offset", "0");
		map.put("length", "100");
		ResponseEntity<String> exchange = restTemplate.exchange(
				API + "/executor?session.id={id}&ajax=fetchExecJobLogs&execid={execid}&jobId={jobId}&offset={offset}&length={length}",
				HttpMethod.GET, new HttpEntity<String>(hs), String.class, map);
		System.out.println(exchange.getBody());
	}

	/**
	 * Fetch Flow Execution Updates 获取执行流的信息状态
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void fetchFlowUpdateTest() throws KeyManagementException, NoSuchAlgorithmException {

		SSLUtil.turnOffSslChecking();
		HttpHeaders hs = new HttpHeaders();
		hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		hs.add("X-Requested-With", "XMLHttpRequest");
		hs.add("Accept", "text/plain;charset=utf-8");

		Map<String, String> map = new HashMap<>();
		map.put("id", "c4adf192-dcf4-4e05-bd08-f6989dc544a7");
		map.put("execid", "11");
		map.put("lastUpdateTime", "-1");
		ResponseEntity<String> exchange = restTemplate.exchange(
				API + "/executor?session.id={id}&ajax=fetchexecflowupdate&execid={execid}&lastUpdateTime={lastUpdateTime}",
				HttpMethod.GET, new HttpEntity<String>(hs), String.class, map);
		System.out.println(exchange.getBody());
	}

}
