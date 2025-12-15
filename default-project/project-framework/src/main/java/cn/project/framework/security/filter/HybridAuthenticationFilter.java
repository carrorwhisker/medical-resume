package cn.project.framework.security.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import cn.project.common.core.domain.model.LoginUser;
import cn.project.common.utils.TokenUtils;
import cn.project.common.utils.SecurityUtils;
import cn.project.common.utils.StringUtils;
import cn.project.framework.web.service.TokenService;

/**
 * 混合认证过滤器
 * 同时支持 JWT Token（后台管理）和 AES Token（小程序）认证
 * 
 * @author default
 */
@Component
public class HybridAuthenticationFilter extends OncePerRequestFilter
{
    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        // 先尝试 JWT 认证（后台管理系统）
        LoginUser loginUser = tokenService.getLoginUser(request);
        
        // 如果 JWT 认证失败，尝试 AES Token 认证（小程序）
        if (StringUtils.isNull(loginUser))
        {
            String aesToken = request.getHeader("Token");
            if (StringUtils.isNotEmpty(aesToken))
            {
                String uid = TokenUtils.getUidFromToken(aesToken);
                if (StringUtils.isNotEmpty(uid))
                {
                    // 创建一个简单的 LoginUser 对象用于小程序用户
                    loginUser = createMiniProgramLoginUser(uid);
                }
            }
        }
        
        // 如果认证成功，设置到 Security Context
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication()))
        {
            // 如果是 JWT Token，验证过期时间
            if (loginUser.getToken() != null)
            {
                tokenService.verifyToken(loginUser);
            }
            
            UsernamePasswordAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        
        chain.doFilter(request, response);
    }
    
    /**
     * 为小程序用户创建 LoginUser 对象
     */
    private LoginUser createMiniProgramLoginUser(String uid)
    {
        // 创建 SysUser 对象
        cn.project.common.core.domain.entity.SysUser sysUser = new cn.project.common.core.domain.entity.SysUser();
        sysUser.setUserId(Long.parseLong(Math.abs(uid.hashCode()) + ""));
        sysUser.setUserName("miniprogram_user_" + uid);
        
        // 创建 LoginUser 对象
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(Long.parseLong(Math.abs(uid.hashCode()) + ""));
        loginUser.setUser(sysUser);
        loginUser.setPermissions(new java.util.HashSet<>());
        
        return loginUser;
    }
}

