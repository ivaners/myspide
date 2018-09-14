package tk.ivanlily.spider;

import tk.ivanlily.manager.ProxyIpManager;
import tk.ivanlily.model.ProxyIp;
import tk.ivanlily.model.bo.ProxyIpBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * 自定义Pipeline处理抓取的数据
 * @author zifangsky
 *
 */
@Component("proxyIPPipeline")
public class ProxyIPPipeline implements Pipeline {
	@Autowired
	private ProxyIpManager proxyIpManager;

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
				if (CheckIPUtils.checkValidIP(proxyIpBO.getIp(), proxyIpBO.getPort())) {
					// 1 查询该IP是否已存在
					ProxyIp oldIP = proxyIpManager.selectByIPPort(proxyIpBO.getIp(), proxyIpBO.getPort());

					// 2如果不存在则插入数据
					if (oldIP == null) {
						proxyIpBO.setUsed(false);
						proxyIpManager.insertSelective(proxyIpBO);
					}
				}
			});
		}

	}

}
