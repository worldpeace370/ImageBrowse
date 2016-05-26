package com.lebron.imagebrowse.bean;

/**图片信息类的java bean json
 * Created by wuxiangkun on 2016/5/21 20:38.
 * Contacts wuxiangkun@live.com
 */
public class ImageInfo {

    /**
     * id : 1
     * name : 立花里子0
     * sex : girl
     * describes : 看起来是不是有羞羞的念头
     * big_img : big_0000.jpg
     * small_img : small_0000.jpg
     */

    private String id;
    private String name;
    private String sex;
    private String describes;
    private String big_img;
    private String small_img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getBig_img() {
        return big_img;
    }

    public void setBig_img(String big_img) {
        this.big_img = big_img;
    }

    public String getSmall_img() {
        return small_img;
    }

    public void setSmall_img(String small_img) {
        this.small_img = small_img;
    }
}
