package com.aliyun.ayland.data;

public class ATSceneTemplateBean {
    /**
     * id : 1
     * templateName : 起床模式
     * templateIcon : 模板图片，现在没有填进去
     * templateSort : 0
     */

    private int id;
    private String templateName;
    private String templateIcon;
    private String templateSort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateIcon() {
        return templateIcon;
    }

    public void setTemplateIcon(String templateIcon) {
        this.templateIcon = templateIcon;
    }

    public String getTemplateSort() {
        return templateSort;
    }

    public void setTemplateSort(String templateSort) {
        this.templateSort = templateSort;
    }
}
