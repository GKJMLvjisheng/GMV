package com.cascv.oas.core.exception;

// basic exception
public class BaseException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    private String module;
    private String code;
    
    private Object[] args;        // 错误码对应的参数
    private String defaultMessage;// 错误消息

    public BaseException(String module, String code, Object[] args, String defaultMessage)
    {
        this.module = module;
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    public BaseException(String module, String code, Object[] args)
    {
        this(module, code, args, null);
    }

    public BaseException(String module, String defaultMessage)
    {
        this(module, null, null, defaultMessage);
    }

    public BaseException(String code, Object[] args)
    {
        this(null, code, args, null);
    }

    public BaseException(String defaultMessage)
    {
        this(null, null, null, defaultMessage);
    }

    @Override
    public String getMessage()
    {
        String message = defaultMessage;
        return message;
    }

    public String getModule()
    {
        return module;
    }

    public String getCode()
    {
        return code;
    }

    public Object[] getArgs()
    {
        return args;
    }

    public String getDefaultMessage()
    {
        return defaultMessage;
    }

  @Override
  public String toString() {
    return this.getClass() + "{" + "module='" + module + '\'' + ", message='" + getMessage() + '\'' + '}';
  }
}
