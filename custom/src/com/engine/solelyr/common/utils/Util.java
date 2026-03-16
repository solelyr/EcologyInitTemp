package com.engine.solelyr.common.utils;

/**
 * @DESCRIPTION:
 * @author: solelyr
 * @data: 2023/8/19 19:09
 * @version: 1.0
 */

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.mzlion.core.lang.CollectionUtils;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Util{

    /**
     * 获取当前日期 yyyy-MM-dd 格式
     * @return
     */
    public static String getNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * 将List<map>转换成List<java对象>
     * * @param list
     * * @param cls
     * * @param <T>
     * * @return
     */
    public static <T> List<T> parseListObject(List<Map<String,Object>> list, Class<T> cls){
        List<T> paramList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)){
            for (Map<String, Object> map : list) {
                paramList.add(parseMapObject(map,cls));
            }
        }
        return paramList;
    }

    /**
     * 将map转换成java对象
     * @param paramMap
     * * @param cls
     * * @param <T>
     * * @return
     * */
    public static <T> T parseMapObject(Map<String,Object> paramMap, Class<T> cls) {
        return JSONObject.parseObject(JSONObject.toJSONString(paramMap),cls);
    }

    private static Random random = new Random();
    public Util() {
    }

    public static boolean str2bool(String str) {
        boolean b = false;

        try {
            if (!"".equals(str)) {
                b = Boolean.valueOf(str);
            }
        } catch (Exception e) {
            b = false;
        }

        return b;
    }

    public static String getCookie(HttpServletRequest request, String name) {
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                for(int i = 0; i < cookies.length; ++i) {
                    if (cookies[i].getName().equals(name)) {
                        return cookies[i].getValue();
                    }
                }
            }
        } catch (Exception e) {
        }

        return null;
    }
    public static void setCookie(HttpServletResponse response, String name, String value, int age, String domain) {
        try {
            value = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
        }

        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(age);
        cookie.setDomain(domain);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void setCookie(HttpServletResponse servletResponse, String name, String value, int age) {
        try {
            value = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            var5.printStackTrace();
        }

        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(age);
        cookie.setPath("/");
        servletResponse.addCookie(cookie);
    }
    public static void setCookie(HttpServletResponse response, String name, String value) {
        setCookie(response, name, value, -1);
    }

    public static String null2String(Object s, String s1) {
        return s == null ? (s1 == null ? "" : s1) : null2String(s);
    }
    public static String null2String(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static boolean contains(Object[] objects, Object o) {
        if (objects != null && o != null) {
            for(int i = 0; i < objects.length; ++i) {
                if (objects[i] != null && objects[i].equals(o)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
    /**
     * 提取 s1和s2之间的字符串
     * @param s
     * @param s1
     * @param s2
     * @return
     */
    public static String extract(String s, String s1, String s2) {
        int i = s1 == null ? 0 : s.indexOf(s1);
        int i1 = s1 == null ? 0 : s1.length();
        int i2 = s2 == null ? s.length() : s.indexOf(s2, i + i1);
        if (i == -1) {
            i = 0;
            i1 = 0;
        }
        if (i2 == -1) {
            i2 = s.length();
        }

        return s.substring(i + i1, i2);
    }
    public static String remove(String s, String s1) {
        int i = s.indexOf(s1);
        int i1 = s1.length();
        return i != -1 ? s.substring(0, i) + s.substring(i + i1) : s;
    }
    public static boolean isEmail(String email) {
        if (email != null && !"".equals(email)) {
            if (email.indexOf(";") < 0 && email.indexOf(",") < 0 && email.indexOf(">") < 0 && email.indexOf("<") < 0 && email.indexOf("[") < 0 && email.indexOf("]") < 0 && email.indexOf(")") < 0 && email.indexOf("(") < 0) {
                boolean b = false;
                int i = email.indexOf("@");
                if (i == -1) {
                    return false;
                } else {
                    return email.substring(i).indexOf(".") >= 0;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String StringReplace(String s, String s1, String s2) {
        s = null2String(s);
        s1 = null2String(s1);
        s2 = null2String(s2);

        try {
            s = s.replace(s1, s2);
        } catch (Exception e) {
        }

        return s;
    }

    public static int getIntValue(Object s) {
        return getIntValue(s, -1);
    }
    public static int getIntValue(Object s, int i) {
        try {
            return Integer.parseInt(null2String(s));
        } catch (Exception e) {
            return i;
        }
    }

    public static float getFloatValue(String s) {
        return getFloatValue(s, -1.0F);
    }

    public static float getFloatValue(String s, float f) {
        try {
            return Float.parseFloat(s);
        } catch (Exception var3) {
            return f;
        }
    }

    public static double getDoubleValue(String s) {
        return getDoubleValue(s, -1.0);
    }

    public static double getDoubleValue(Object s, double d) {
        try {
            return Double.parseDouble(null2String(s));
        } catch (Exception var4) {
            return d;
        }
    }

    public static String getPointValue(String s) {
        return getPointValue(s, 2);
    }

    public static String getPointValue(String s, int i) {
        return getPointValue(s, i, "-1");
    }

    public static String getPointValue(String s, int i, String s1) {
        try {
            Double.parseDouble(s);
            String s2 = s;
            if (s.indexOf("E") != -1) {
                s2 = getfloatToString(s);
            }

            int i1;
            if (s2.indexOf(".") == -1) {
                s2 = s2 + ".";

                for(i1 = 0; i1 < i; ++i1) {
                    s2 = s2 + "0";
                }
            } else if (s2.length() - s2.lastIndexOf(".") <= i) {
                for(i1 = 0; i1 < i - s2.length() + s2.lastIndexOf(".") + 1; ++i1) {
                    s2 = s2 + "0";
                }
            } else {
                s2 = s2.substring(0, s2.lastIndexOf(".") + i + 1);
            }

            return s2;
        } catch (Exception e) {
            return s1;
        }
    }

    public static String getRandom() {
        int i;
        for(i = 1000000000 + random.nextInt(1000000000); i == 0; i = 1000000000 + random.nextInt(1000000000)) {
        }

        return String.valueOf(i);
    }

    public static String getPortalPassword(){
        return getPortalPassword(8);
    }

    public static String getPortalPassword(int length) {
        if(length<=0) length=8;
        String s = "";
        boolean b = false;

        for(int i = 0; i < length; ++i) {
            char c;
            if (!b) {
                b = true;
                c = (char)((int)(Math.random() * 26.0 + 97.0));
                s = s + c;
            } else {
                b = false;
                c = (char)((int)(Math.random() * 10.0 + 48.0));
                s = s + c;
            }
        }

        return s;
    }

    public static String getfloatToString(String s) {
        boolean b = false;
        if (s.indexOf("-") != -1) {
            b = true;
            s = s.substring(1, s.length());
        }

        int i = s.indexOf("E");
        if (i == -1) {
            return s;
        } else {
            int i1 = Integer.parseInt(s.substring(i + 1, s.length()));
            s = s.substring(0, i);
            i = s.indexOf(".");
            s = s.substring(0, i) + s.substring(i + 1, s.length());
            String s1 = s;
            if (s.length() <= i1) {
                for(int var5 = 0; var5 < i1 - s.length() + 1; ++var5) {
                    s1 = s1 + "0";
                }
            } else {
                s1 = s.substring(0, i1 + 1) + "." + s.substring(i1 + 1) + "0";
            }

            if (b) {
                s1 = "-" + s1;
            }

            return s1;
        }
    }

    public static String getRequestHost(HttpServletRequest request) {
        return null2String(request.getHeader("Host")).trim();
    }

    public static String replaceString(String s, String s1, String s2) {
        if (s == null) {
            return s;
        } else {
            int i = 0;
            if ((i = s.indexOf(s1, i)) >= 0 && s2 != null) {
                char[] chars = s.toCharArray();
                char[] chars1 = s2.toCharArray();
                int i1 = s1.length();
                StringBuffer stringBuffer = new StringBuffer(chars.length);
                stringBuffer.append(chars, 0, i).append(chars1);
                i += i1;

                int i2;
                for(i2 = i; (i = s.indexOf(s1, i)) > 0; i2 = i) {
                    stringBuffer.append(chars, i2, i - i2).append(chars1);
                    i += i1;
                }

                stringBuffer.append(chars, i2, chars.length - i2);
                return stringBuffer.toString();
            } else {
                return s;
            }
        }
    }

    public static String getRandomSm4UUID() {
        UUID uuid = UUID.randomUUID();
        return "sm4start" + uuid + "sm4end";
    }

    public static String[] splitString(String s, String s1) {
        return splitString(s, s1, -1);
    }

    public static String[] splitString(String s, String s1, int i) {
        List list = splitString2List(s, s1, i);
        String[] strings = new String[list.size()];
        return (String[])list.toArray(strings);
    }

    public static List<String> splitString2List(String s, String s1) {
        return splitString2List(s, s1, -1);
    }

    public static List<String> splitString2List(String s, String s1, int i) {
        int length = s1.length();
        int i1 = 0;
        boolean b = false;
        boolean b1 = i > 0;

        ArrayList arrayList;
        int i2;
        for(arrayList = new ArrayList(); (i2 = s.indexOf(s1, i1)) != -1; i1 = i2 + length) {
            if (b1 && arrayList.size() >= i - 1) {
                arrayList.add(s.substring(i1, s.length()));
                i1 = s.length();
                break;
            }

            arrayList.add(s.substring(i1, i2));
        }

        if (i1 == 0) {
            arrayList.add(s);
            return arrayList;
        } else {
            if (!b1 || arrayList.size() < i) {
                arrayList.add(s.substring(i1, s.length()));
            }

            int size = arrayList.size();
            if (i == 0) {
                while(size > 0 && ((String)arrayList.get(size - 1)).length() == 0) {
                    --size;
                }
            }

            return arrayList.subList(0, size);
        }
    }
    public static String formatDouble(double d) {
        String str = String.valueOf(d);
        if (str.length() < 8) {
            return str;
        } else {
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(20);
            numberFormat.setGroupingUsed(false);
            return numberFormat.format(d);
        }
    }

    public static String formatDecimal(String str) {
        if (str == null) {
            return "null";
        } else if (str.length() < 8) {
            return str;
        } else {
            str = str.trim();
            if ("".equals(str)) {
                return "";
            } else if (str.indexOf(".") < 0) {
                return str;
            } else if (str.endsWith(".")) {
                return str;
            } else {
                String[] strings = str.split("\\.");
                String s = strings[0];
                String s1 = strings[1];
                if (s1.length() == 1) {
                    return "0".equals(s1) ? s : str;
                } else {
                    for(int i = s1.length() - 1; i > 1 && s1.charAt(i) == '0'; --i) {
                        s1 = s1.substring(0, i);
                    }
                    return "00".equals(s1) ? s : s + "." + s1;
                }
            }
        }
    }
    public static String formatParams(Object... params) {
        if(params == null) return null;
        ArrayList arrayList = new ArrayList();
        Object[] objects = params;
        int length = params.length;

        for(int i = 0; i < length; ++i) {
            Object object = objects[i];
            Iterator iterator;
            Object obj;
            if (object instanceof List) {
                iterator = ((List)object).iterator();

                while(iterator.hasNext()) {
                    obj = iterator.next();
                    arrayList.add(obj);
                }
            } else if (object instanceof Vector) {
                iterator = ((Vector)object).iterator();

                while(iterator.hasNext()) {
                    obj = iterator.next();
                    arrayList.add(obj);
                }
            } else {
                arrayList.add(object);
            }
        }

        return arrayList.toString();
    }

    public static StackTraceElement findCaller(int x) {
        // 获取堆栈信息
        StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
        // 最原始被调用的堆栈信息
        StackTraceElement caller = null;
        // 日志类名称
        String className = Util.class.getName();
        // 循环遍历到日志类标识
        int i = 0;
        for (int len = callStack.length; i < len; i++) {
            // 找到当前类所在堆栈
            if (className.equals(callStack[i].getClassName())) {
                break;
            }
        }
        //是的没有错,这是一个magic number！想知道为什么？开启你的堆栈，来寻找我的宝藏吧！by Luo
        caller = callStack[i + x];
        return caller;
    }

    public static Map<String, Object> request2Map(HttpServletRequest request) {
        Map map = request.getParameterMap();
        HashMap hashMap = new HashMap();
        Iterator iterator = map.entrySet().iterator();
        String str = "";

        for(Object object = null; iterator.hasNext(); hashMap.put(str, object)) {
            Map.Entry next = (Map.Entry)iterator.next();
            str = (String)next.getKey();
            Object value = next.getValue();
            if (null == value) {
                object = null;
            } else if (value instanceof String[]) {
                String[] strs = (String[])((String[])value);
                if (strs.length == 1) {
                    object = strs[0];
                } else {
                    object = strs;
                }
            } else if (value instanceof Map) {
                object = JSONObject.parseObject((String)value, new TypeReference<Map<String, Object>>() {
                }, new Feature[0]);
            } else {
                object = value.toString();
            }
        }
        Map<String,Object> data = new HashMap<>();
        data.put("data",hashMap);
        data.put("ip", Util.getIpAddr(request));
        data.put("user_agent", request.getHeader("user-agent"));
        return data;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String header = request.getHeader("x-forwarded-for");
        if (header == null || header.length() == 0 || "unknown".equalsIgnoreCase(header)) {
            header = request.getHeader("Proxy-Client-IP");
        }

        if (header == null || header.length() == 0 || "unknown".equalsIgnoreCase(header)) {
            header = request.getHeader("WL-Proxy-Client-IP");
        }

        if (header == null || header.length() == 0 || "unknown".equalsIgnoreCase(header)) {
            header = request.getRemoteAddr();
        }

        if (header.indexOf(",") >= 0) {
            header = header.substring(0, header.indexOf(","));
        }

        return header;
    }

    /**
     * 将map的key全部转换为小写
     * @param orgMap
     * @return
     */
    public static Map<String, Object> transformLowerCase(Map<String, Object> orgMap) {
        Map<String, Object> resultMap = new HashMap<>();
        if (orgMap == null || orgMap.isEmpty()) {
            return resultMap;
        }
        Set<String> keySet = orgMap.keySet();
        for (String key : keySet) {
            String newKey = key.toLowerCase();
            resultMap.put(newKey, orgMap.get(key));
        }
        return resultMap;
    }

    public static String mapToUrl(Map<String, Object> map) {
        List<String> list = new ArrayList<>();
        for (String key : map.keySet()) {
            String value = null2String(map.get(key));
            if(!"".equals(value)) list.add(key+"="+value);
        }
        if (list.size() > 0) {
            return "?" + String.join("&", list);
        }else {
            return "";
        }
    }

    /**
     * 转换一下系统中的空格和换行
     * @param input
     * @return
     */
    public static String convertDB2Input(String input) {
        return "".equals(null2String(input)) ? input : input.replaceAll("&nbsp;"," ").replaceAll("<br>","");
    }

}
