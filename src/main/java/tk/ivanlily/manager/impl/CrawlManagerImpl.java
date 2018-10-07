package tk.ivanlily.manager.impl;

import javax.annotation.Resource;

import tk.ivanlily.common.CrowProxyProvider;
import tk.ivanlily.common.PageInfo;
import tk.ivanlily.manager.ProxyIpManager;
import tk.ivanlily.model.ProxyIp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.ivanlily.manager.CrawlManager;
import tk.ivanlily.spider.ProxyIPPipeline;
import tk.ivanlily.spider.ProxyIPSpider;
import tk.ivanlily.spider.ProxyIPSpider2;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.proxy.Proxy;

import java.util.ArrayList;
import java.util.List;

@Service("crawlManager")
public class CrawlManagerImpl implements CrawlManager {
	@Autowired
	private ProxyIpManager proxyIpManager;
	

	@Resource(name="proxyIPPipeline")
	private ProxyIPPipeline proxyIPPipeline;
	
	@Override
	public void proxyIPCrawl() {
		OOSpider.create(new ProxyIPSpider())
				//.setDownloader(proxy())
		.addUrl("http://www.xicidaili.com/nn/1").addPipeline(proxyIPPipeline)
		.thread(3)
		.run();
	}

	@Override
	public void proxyIPCrawl2() {
		OOSpider.create(new ProxyIPSpider2())
				//.setDownloader(proxy())
		.addUrl("http://www.kuaidaili.com/free/inha/1/").addPipeline(proxyIPPipeline)
		.thread(2)
		.run();
	}

	private HttpClientDownloader proxy(){
		PageInfo pageInfo = new PageInfo();
		pageInfo.setFrom(0L);
		pageInfo.setPerSize(200L);
		ProxyIp proxyIpWhere = new ProxyIp();
		proxyIpWhere.setType("HTTPS");
		List<ProxyIp> proxyList = proxyIpManager.findAll(pageInfo, proxyIpWhere);
		List<Proxy> proxies = new ArrayList<>(proxyList.size());
		for(ProxyIp proxyIp : proxyList){
			proxies.add(new Proxy(proxyIp.getIp(), proxyIp.getPort()));
		}
		HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
		httpClientDownloader.setProxyProvider(CrowProxyProvider.from(proxies.toArray(new Proxy[proxies.size()])));
		return httpClientDownloader;
	}
}
