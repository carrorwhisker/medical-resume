package cn.project.web.controller.medical;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.project.common.core.controller.BaseController;
import cn.project.common.core.domain.AjaxResult;
import cn.project.common.core.page.TableDataInfo;
import cn.project.common.utils.SecurityUtils;
import cn.project.common.utils.StringUtils;
import cn.project.common.annotation.Log;
import cn.project.common.enums.BusinessType;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * 医简历管理员控制器
 * 
 * @author medical
 */
@RestController
@RequestMapping("/Main/action/Admin/Admin")
public class AdminController extends BaseController
{
    
    /**
     * 管理员登录
     */
    @PostMapping("/adminLogin")
    public AjaxResult adminLogin(@RequestBody Map<String, Object> params)
    {
        String name = (String) params.get("name");
        String password = (String) params.get("password");
        
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password))
        {
            return AjaxResult.error("请输入账号和密码");
        }
        
        // TODO: 实现管理员登录逻辑
        // 1. 验证账号密码
        // 2. 生成token
        // 3. 返回登录信息
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", "admin_token_example");
        result.put("is_root", 0);
        
        return AjaxResult.success("登录成功", result);
    }
    
    /**
     * 获取管理员列表
     */
    @PostMapping("/getAdminList")
    @PreAuthorize("@ss.hasPermi('medical:admin:list')")
    public TableDataInfo getAdminList(@RequestBody Map<String, Object> params)
    {
        // TODO: 权限验证
        
        Integer size = (Integer) params.getOrDefault("size", 20);
        Integer page = (Integer) params.getOrDefault("page", 1);
        
        startPage();
        
        // TODO: 实现获取管理员列表逻辑
        List<Map<String, Object>> list = new ArrayList<>(); // 空列表示例
        
        return getDataTable(list);
    }
    
    /**
     * 添加管理员
     */
    @PostMapping("/addAdmin")
    @PreAuthorize("@ss.hasPermi('medical:admin:add')")
    @Log(title = "管理员管理", businessType = BusinessType.INSERT)
    public AjaxResult addAdmin(@RequestBody Map<String, Object> params)
    {
        // TODO: 权限验证
        
        String name = (String) params.get("name");
        String password = (String) params.get("password");
        
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password))
        {
            return AjaxResult.error("请输入完整信息");
        }
        
        // TODO: 实现添加管理员逻辑
        // 1. 密码加密
        // 2. 生成uid
        // 3. 插入数据库
        
        return AjaxResult.success("添加成功");
    }
    
    /**
     * 修改管理员
     */
    @PostMapping("/editAdmin")
    @PreAuthorize("@ss.hasPermi('medical:admin:edit')")
    @Log(title = "管理员管理", businessType = BusinessType.UPDATE)
    public AjaxResult editAdmin(@RequestBody Map<String, Object> params)
    {
        // TODO: 权限验证
        
        String id = (String) params.get("id");
        String name = (String) params.get("name");
        String password = (String) params.get("password");
        
        if (StringUtils.isEmpty(id))
        {
            return AjaxResult.error("请输入id");
        }
        
        // TODO: 实现修改管理员逻辑
        
        return AjaxResult.success("修改成功");
    }
    
    /**
     * 删除管理员
     */
    @PostMapping("/delAdmin")
    @PreAuthorize("@ss.hasPermi('medical:admin:remove')")
    @Log(title = "管理员管理", businessType = BusinessType.DELETE)
    public AjaxResult delAdmin(@RequestBody Map<String, Object> params)
    {
        // TODO: 权限验证
        
        String id = (String) params.get("id");
        
        if (StringUtils.isEmpty(id))
        {
            return AjaxResult.error("请输入id");
        }
        
        // TODO: 实现删除管理员逻辑
        
        return AjaxResult.success("删除成功");
    }
}
