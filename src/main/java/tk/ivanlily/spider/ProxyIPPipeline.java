package tk.ivanlily.spider;

import org.springframework.beans.factory.annotation.Value;
import tk.ivanlily.manager.ProxyIpManager;
import tk.ivanlily.model.ProxyIp;
import tk.ivanlily.model.bo.ProxyIpBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.ivanlily.mq.producer.CheckIPSender;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;
import java.util.List;

/**
 * 自定义Pipeline处理抓取的数据
 * @author zifangsky
 *
 */
@Component("proxyIPPipeline")
public class ProxyIPPipeline implements Pipeline {
	@Resource(name="checkIPSender")
	private CheckIPSender checkIPSender;

	@Autowired
	private ProxyIpManager proxyIpManager;

	@Value("${mq.topicName.checkIP}")
	private String checkIPTopicName;


	/**
	 * 保存数据
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {
		List<ProxyIp> list = resultItems.get("result");
		
		if(list != null && list.size() > 0){
			list.forEach(proxyIp -> {
				ProxyIpBO proxyIpBO = new ProxyIpBO();
				proxyIpBO.setId(proxyIp.getId());
				proxyIpBO.setIp(proxyIp.getIp());
				proxyIpBO.setPort(proxyIp.getPort());
				proxyIpBO.setType(proxyIp.getType());
				proxyIpBO.setAddr(proxyIp.getAddr());
				proxyIpBO.setUsed(proxyIp.getUsed());
				proxyIpBO.setOther(proxyIp.getOther());
				proxyIpBO.setCheckType(ProxyIpBO.CheckIPType.ADD);

				//检测任务添加到队列中
				checkIPSender.send(checkIPTopicName, proxyIpBO);
			});
		}

	}

}
