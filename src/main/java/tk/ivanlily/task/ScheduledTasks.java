package tk.ivanlily.task;

import org.springframework.beans.factory.annotation.Value;
import tk.ivanlily.manager.CrawlManager;
import tk.ivanlily.manager.ProxyIpManager;
import tk.ivanlily.mapper.ProxyIpMapper;
import tk.ivanlily.model.ProxyIp;
import tk.ivanlily.model.bo.ProxyIpBO;
import tk.ivanlily.mq.producer.CheckIPSender;
import tk.ivanlily.spider.CheckIPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 定时任务配置
 *
 * @author zifangsky
 * @date 2018/6/21
 * @since 1.0.0
 */
@Component
public class ScheduledTasks {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);
    private final Format FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Resource(name="proxyIpManager")
    private ProxyIpManager proxyIpManager;


    @Resource(name="crawlManager")
    private CrawlManager crawlManager;

    @Autowired
    protected ProxyIpMapper proxyIpMapper;

    @Resource(name="checkIPSender")
    private CheckIPSender checkIPSender;

    @Value("${mq.topicName.checkIP}")
    private String checkIPTopicName;


    /**
     * 代理IP定时检测任务（检查是否有效）
     * @author zifangsky
     * @date 2018/6/21 13:50
     * @since 1.0.0
     */
    @Scheduled(cron = "${task.checkProxyIp.schedule}")
    public void checkProxyIpTask(){
        Date current = new Date();
        LOGGER.debug(MessageFormat.format("开始执行代理IP定时检测任务，Date：{0}",FORMAT.format(current)));

        //1 查询数据库中所有代理IP
        List<ProxyIp> list = proxyIpManager.selectAll();

        if(list != null && list.size() > 0){
            //2 遍历
            list.forEach(proxyIp -> {
                ProxyIpBO proxyIpBO = new ProxyIpBO();
                proxyIpBO.setId(proxyIp.getId());
                proxyIpBO.setIp(proxyIp.getIp());
                proxyIpBO.setPort(proxyIp.getPort());
                proxyIpBO.setType(proxyIp.getType());
                proxyIpBO.setAddr(proxyIp.getAddr());
                proxyIpBO.setUsed(proxyIp.getUsed());
                proxyIpBO.setOther(proxyIp.getOther());
                proxyIpBO.setCheckType(ProxyIpBO.CheckIPType.UPDATE);

                checkIPSender.send(checkIPTopicName, proxyIpBO);
                if (!CheckIPUtils.checkValidIP(proxyIpBO.getIp(), proxyIpBO.getPort())) {
                    proxyIpManager.deleteByPrimaryKey(proxyIpBO.getId());
                }
            });
        }
    }

    /**
     * 代理IP定时获取任务1
     * @author zifangsky
     * @date 2018/6/21 13:53
     * @since 1.0.0
     */
    @Scheduled(cron = "${task.crawlProxyIp_1.schedule}")
    public void crawlProxyIpTask1(){
        Date current = new Date();
        LOGGER.debug(MessageFormat.format("开始执行代理IP定时获取任务1，Date：{0}",FORMAT.format(current)));

        crawlManager.proxyIPCrawl();
    }

    /**
     * 代理IP定时获取任务2
     * @author zifangsky
     * @date 2018/6/21 13:55
     * @since 1.0.0
     */
    @Scheduled(cron = "${task.crawlProxyIp_2.schedule}")
    public void crawlProxyIpTask2(){
        Date current = new Date();
        LOGGER.debug(MessageFormat.format("开始执行代理IP定时获取任务2，Date：{0}",FORMAT.format(current)));

        crawlManager.proxyIPCrawl2();
    }
}
