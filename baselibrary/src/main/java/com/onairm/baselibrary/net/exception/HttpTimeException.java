package com.onairm.baselibrary.net.exception;

/**
 * 自定义错误信息，统一处理返回处理
 * Created by WZG on 2016/7/16.
 *
 */
public class HttpTimeException extends RuntimeException {

    public HttpTimeException(int resultCode) {
//        this(getApiExceptionMessage(resultCode));
        super(getApiExceptionMessage(resultCode));
    }

    public HttpTimeException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 转换错误数据
     *
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code) {
        String message = "";
        switch (code) {
            case 1000:
                message = "未知错误";//未知错误（系统错误）
                break;
            case 1001:
                message = "访问受限";//IP校验：IP访问频繁被加入黑名单
                break;
            case 1002:
                message = "访问受限";//IP校验：此IP不在白名单内
                break;
            case 1010:
                message = "权限找不到目标文件";//找不到目标文件：
                break;
            case 1011:
                message = "权限不足";//访问目标文件权限不足：
                break;
            case 1100:
                message = "参数校验失败";//参数校验失败: 未查明原因
                break;
            case 1101:
                message = "参数个数不正确";//参数校验失败：个数不正确
                break;
            case 1102:
                message = "未知参数";//参数校验失败：未知参数
                break;
            case 1103:
                message = "参数不能为空";//参数校验失败：参数不能为空
                break;
            case 1104:
                message = "参数长度限制";//参数校验失败：参数长度限制
                break;
            case 1105:
                message = "参数值不正确";//参数校验失败：特定参数出现未预期的值
                break;
            case 1106:
                message = "邮箱格式有误";//参数校验失败：邮箱格式有误
                break;
            case 1107:
                message = "手机号码格式有误";//参数校验失败：手机号码格式有误
                break;
            case 1108:
                message = "参数tn校验失败";//参数tn校验失败
                break;
            case 1110:
                message = "获取验证码失败";//获取验证码失败
                break;
            case 1111:
                message = "获取验证码次数过多，请稍候再试";//今日获取验证码受限
                break;

            case 2000:
                message = "账号登录失败";//用户账号：账号登录失败
                break;
            case 2001:
                message = "账号未登录";//用户账号：账号未登录
                break;
            case 2002:
                message = "账号过期,请重新登录";//用户账号：账号过期,请重新登录
                break;
            case 2003:
                message = "账号注册失败";//用户账号：账号注册失败
                break;
            case 2004:
                message = "账号未注册";//用户账号：账号未注册
                break;
            case 2005:
                message = "账号已注册";//用户账号：账号已注册
                break;
            case 2006:
                message = "已被绑定账号";//用户账号：已被绑定账号
                break;
            case 2007:
                message = "未绑定账号";//用户账号：未绑定账号
                break;
            case 2008:
                message = "绑定失败";//用户账号：绑定失败
                break;
            case 2009:
                message = "解绑失败";//用户账号：解绑失败
                break;
            case 2010:
                message = "账号被锁定";//用户账号：账号被锁定
                break;
            case 2011:
                message = "账号被禁用";//用户账号：账号被禁用
                break;
            case 2012:
                message = "获取失败";//用户账号：获取用户信息失败
                break;
            case 2013:
                message = "更新失败";//用户账号：更新用户信息失败
                break;
            case 2014:
                message = "用户权限不足";//用户账号：用户权限不足
                break;
            case 2015:
                message = "密码有误";//用户账号：密码有误
                break;
            case 2016:
                message = "修改密码失败";//用户账号：修改密码失败
                break;
            case 2017:
                message = "找回密码失败";//用户账号：找回密码失败
                break;

            case 2100:
                message = "对方不在线";//他人账户：不在线
                break;
            case 2101:
                message = "获取信息失败";//他人账户：获取信息失败
                break;
            case 2102:
                message = "加好友失败";//他人账户：加好友失败
                break;
            case 2103:
                message = "删除失败";//他人账户：删除失败
                break;
            case 2104:
                message = "关注失败";//他人账户：关注失败
                break;
            case 2105:
                message = "取消关注失败";//他人账户：取消关注失败
                break;
            case 2106:
                message = "拉黑失败";//他人账户：拉黑失败
                break;
            case 2107:
                message = "点赞失败";//他人账户：点赞失败
                break;
            case 2108:
                message = "取消点赞失败";//他人账户：取消点赞失败
                break;

            case 3000:
                message = "评论失败";//评论:评论失败
                break;
            case 3001:
                message = "删除评论失败";//评论：删除评论失败
                break;
            case 3002:
                message = "修改评论失败";//评论：修改评论失败
                break;
            case 3003:
                message = "获取评论失败";//评论：获取评论失败
                break;
            case 3004:
                message = "获取评论列表失败";//评论：获取评论列表失败
                break;

            case 3010:
                message = "点赞失败";//点赞：点赞失败
                break;
            case 3011:
                message = "取消点赞失败";//点赞：取消点赞失败
                break;
            case 3012:
                message = "点赞修改失败";//点赞：点赞修改失败
                break;
            case 3013:
                message = "获取点赞失败";//点赞：获取点赞失败
                break;
            case 3014:
                message = "获取点赞列表失败";//点赞：获取点赞列表失败
                break;

            case 3020:
                message = "收藏失败";//收藏：收藏失败
                break;
            case 3021:
                message = "取消收藏失败";//收藏：取消收藏失败
                break;
            case 3022:
                message = "收藏修改失败";//收藏：收藏修改失败
                break;
            case 3023:
                message = "获取收藏失败";//收藏：获取收藏失败
                break;
            case 3024:
                message = "获取收藏列表失败";//收藏：获取收藏列表失败
                break;

            case 3990:
                message = "增加失败";//增加失败
                break;
            case 3991:
                message = "删除失败";//删除失败
                break;
            case 3992:
                message = "更新失败";//更新失败
                break;
            case 3993:
                message = "获取失败";//获取失败
                break;
            case 3994:
                message = "获取列表失败";//获取列表失败
                break;

            default:
                message = "error";
                break;
        }
        return message;
    }
}

