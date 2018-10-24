package com.oases.provider.router

/*
    模块路由 路径定义
 */
object RouterPath{
    //用户模块
    class App{
        companion object {
            const val PATH_MAIN ="/app/main"
        }
    }
    class UserCenter{
        companion object {
            const val PATH_LOGIN = "/userCenter/login"
        }
    }

    class ComputingPower{
        companion object {
            const val PATH_ComputingPower ="/computingPower/main"
           // const val PATH_MAIN ="computingPower/main"
        }
    }

    //订单模块
    class OrderCenter{
        companion object {
            const val PATH_ORDER_CONFIRM = "/orderCenter/confirm"
        }
    }

    //支付模块
    class PaySDK{
        companion object {
            const val PATH_PAY = "/paySDK/pay"
        }
    }

    //消息模块
    class MessageCenter{
        companion object {
            const val PATH_MESSAGE_PUSH = "/message/push"
            const val PATH_MESSAGE_ORDER = "/messageCenter/order"
        }
    }
}
