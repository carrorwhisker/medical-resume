package cn.project.web.controller.medical;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.project.common.core.controller.BaseController;
import cn.project.common.core.domain.AjaxResult;
import cn.project.common.utils.StringUtils;
import cn.project.common.core.page.TableDataInfo;
import cn.project.common.core.text.Convert;
import cn.project.system.domain.medical.MedicalUser;
import cn.project.system.service.medical.IMedicalUserService;

/**
 * 医简历首页控制器
 * 
 * @author medical
 */
@RestController
@RequestMapping("/Main/action/Home/Home")
public class HomeController extends BaseController
{
    @Autowired
    private IMedicalUserService userService;
    
    
    /**
     * 获取轮播图
     */
    @PostMapping("/getBanner")
    public AjaxResult getBanner()
    {
        List<String> banner = new ArrayList<>();
        banner.add("https://bdb-img-1326624442.cos.ap-beijing.myqcloud.com/uploads/WechatIMG4387.jpg");
        banner.add("https://bdb-img-1326624442.cos.ap-beijing.myqcloud.com/uploads/WechatIMG4388.jpg");
        banner.add("https://bdb-img-1326624442.cos.ap-beijing.myqcloud.com/uploads/WechatIMG4389.jpg");
        
        return AjaxResult.success("success", banner);
    }
    
    /**
     * 获取VIP套餐列表
     */
    @PostMapping("/getVipList")
    public AjaxResult getVipList()
    {
        List<Map<String, Object>> vipList = new ArrayList<>();
        
        Map<String, Object> vip1 = new HashMap<>();
        vip1.put("id", 1);
        vip1.put("price", 19.00);
        vip1.put("desc", "1个月");
        vipList.add(vip1);
        
        Map<String, Object> vip2 = new HashMap<>();
        vip2.put("id", 2);
        vip2.put("price", 49.00);
        vip2.put("desc", "3个月");
        vipList.add(vip2);
        
        Map<String, Object> vip3 = new HashMap<>();
        vip3.put("id", 3);
        vip3.put("price", 188.00);
        vip3.put("desc", "12个月");
        vipList.add(vip3);
        
        return AjaxResult.success("success", vipList);
    }
    
    /**
     * 获取小程序用户列表（管理端查看）
     */
    @GetMapping("/getUserList")
    public TableDataInfo getUserList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                   @RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "phone", required = false) String phone)
    {
        startPage();
        MedicalUser query = new MedicalUser();
        if (StringUtils.isNotEmpty(name))
        {
            query.setUsername(name);
        }
        if (StringUtils.isNotEmpty(phone))
        {
            query.setPhone(phone);
        }
        List<MedicalUser> list = userService.selectUserList(query);
        return getDataTable(list);
    }
    
    /**
     * 管理端：保存用户信息（兼容旧接口 userListSave）
     */
    @GetMapping("/userListSave")
    public AjaxResult userListSave(MedicalUser req)
    {
        if (req.getId() == null && (req.getUid() == null || req.getUid().isEmpty()))
        {
            return AjaxResult.error("参数错误");
        }
        // 尝试通过id或uid查询
        MedicalUser user = null;
        if (req.getId() != null)
        {
            user = userService.selectUserByUid(req.getUid());
        }
        if (user == null && req.getUid() != null)
        {
            user = userService.selectUserByUid(req.getUid());
        }
        if (user == null)
        {
            return AjaxResult.error("用户不存在");
        }
        
        if (req.getUsername() != null) user.setUsername(req.getUsername());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        if (req.getImg() != null) user.setImg(req.getImg());
        if (req.getContent() != null) user.setContent(req.getContent());
        if (req.getWechatPhone() != null) user.setWechatPhone(req.getWechatPhone());
        if (req.getIsVip() != null) user.setIsVip(req.getIsVip());
        if (req.getVipTime() != null) user.setVipTime(req.getVipTime());
        user.setUpdateTimeStamp(System.currentTimeMillis() / 1000);
        
        int rows = userService.updateUser(user);
        return rows > 0 ? AjaxResult.success("success") : AjaxResult.error("保存失败");
    }
    
    /**
     * 管理端：删除用户（兼容旧接口 delUserList）
     * 这里只做逻辑返回，未做物理删除，若需物理删除请在Service层实现
     */
    @GetMapping("/delUserList")
    public AjaxResult delUserList(@RequestParam(value = "id", required = false) Long id,
                                  @RequestParam(value = "uid", required = false) String uid)
    {
        if (id == null && (uid == null || uid.isEmpty()))
        {
            return AjaxResult.error("参数错误");
        }
        int rows = 0;
        if (id != null)
        {
            rows = userService.deleteUserById(id);
        }
        else
        {
            rows = userService.deleteUserByUid(uid);
        }
        return rows > 0 ? AjaxResult.success("success") : AjaxResult.error("删除失败或用户不存在");
    }
}
