package com.oases.base.common

import java.math.BigDecimal

/*
    基础常量
 */
class BaseConstant{
    companion object {
        //七牛服务地址
        const val IMAGE_SERVER_ADDRESS = "http://osea2fxp7.bkt.clouddn.com/"

        //本地服务器地址
        //const val SERVER_ADDRESS = "https://10.0.0.20"
        //远程服务器地址
         //const val SERVER_ADDRESS = "https://oas.cascv.com"//"http://18.219.19.160:8080"/"http://10.0.0.13:8080"//
        const val SERVER_ADDRESS = "https://dapp.oases.pro"    //正式生产环境

        //SP表名
        const val TABLE_PREFS = "Kotlin_mall"
        //Token Key
        const val USER_TOKEN = "token"
        const val USER_NAME = "user_name"
        const val USER_ICON = "user_icon"
        const val USER_NICK_NAME = "user_nick_name"
        const val USER_ID = "user_id"
        const val USER_PASSWORD = "user_password"
        const val USER_GENDER = "user_gender"
        const val USER_ADDRESS = "user_address"
        const val USER_BIRTHDAY = "user_birthday"
        const val USER_PHONE_NUMBER = "user_phone_number"
        const val USER_MAIL_ADDRESS = "user_mail_address"
        const val USER_INVITE_CODE = "user_invite_code"
        const val USER_UUID = "uuid"
        const val WALLET_BACKUP = "wallet_backup"

        const val MY_OAS_ADDRESS = "my_oas_address"
        const val MY_OAS_AMOUNT = "my_oas_amount"
        const val MY_OAS_AMOUNT_UNCONFIRMED = "my_oas_amount_unconfirmed"
        const val MY_POINTS = "MY_POINTS"
        const val MY_OAS_PROTOCOL = "my_oas_protocol"
        //const val ON_GOING_TRANSACTION="on_going_Transaction"
        const val USER_OWN_ETH ="user_own_eth"
        //const val MY_OAS_REDRAW = 2000 //提币默认的邮费
        const val TRANSFER_NET = "transfer_net" //流通的网络ropsten.
        const val TRANSFER_CHECK_ADDRESS = "etherscan.io/tx/" //流通的网络地址

        const val TOTAL_ITEM = "total_item"
        const val OUT_ITEM = "out_item"
        const val IN_ITEM = "in_item"

        const val PAGE_SIZE = 20 //100
        const val GAS_LIMIT = 60000
        const val GAS_AMOUNT = 37247
        const val GAS_PRICE_LOW = 1 //seekbar显示的最小值
        const val GAS_PRICE_HIGH = 10//seekbar显示的最大值

        /*正则表达式*/
        //大于0的整数小数
        const val  POSITIVE_NUMER = "^([1-9][0-9]*)+(.[0-9]*)?$"
        //英文和数字
        const val ENGLISH_NUMBER = "^[A-Za-z0-9]+$"
        //数字小数点后面最多四位
        const val NUMBER_POINT_FOUR ="^((([1-9][0-9]*)+)|(0{1}))(.[0-9]{0,4})?$"//"^\\d+(\\.\\d{1,4})?$"

        //更多记录的类型
        const val MORE_TYPE = "more_type"
        const val FRAGMENT_INDEX = "fragment_index"

        const val PACKAGE_URL = "package_url"
        //const val APP_VERSION = "app_version"
        //const val APP_VERSION_NAME="app_version_name"

        const val LAST_STEP_UPLOAD_DATE = "last_step_upload_date"
        const val LOADING_STAY_TIME = 2000

    }
}
