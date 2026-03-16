package com.engine.solelyr.common.utils;

import com.engine.solelyr.common.entity.FileEntity;
import com.engine.solelyr.common.entity.ImageEntity;
import com.finance.system.toolkit.DesUtils;
import weaver.conn.RecordSet;
import java.util.ArrayList;
import java.util.List;


/**
 * @DESCRIPTION: 文件工具类
 * @USER: solelyr
 * @DATE: 2025/9/23 上午11:24
 */
public class FileUtil {
    /**
     * 生成可供第三方下载的附件对象
     * @param url
     * @param docIdS
     */
    public List<FileEntity> createFileById(String url, String docIdS) {
        try {
            List<FileEntity> fileList = new ArrayList<>();
            for (String docId : docIdS.split(",")) {
                ImageEntity image = getImageFileName(Util.getIntValue(docId));
                String rawCode = "1_" + image.getImageFileId();
                DesUtils des = null;
                String ddcode; // 生成加密校验码
                des = new DesUtils();
                ddcode = des.encrypt(rawCode);
                fileList.add(new FileEntity(image.getImageFileName(),url+"/weaver/weaver.file.FileDownload?fileid="+image.getImageFileId()+"&download=1&ddcode="+ddcode));
            }
            return fileList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据ID获取附件名称
     * @param docId
     * @return
     */
    public ImageEntity getImageFileName(int docId){
        String sql = "select a.ID,a.accessorycount,c.imagefileid,c.imagefilename,c.filerealpath from docdetail a inner join docimagefile b on a.id = b.docid inner join imagefile c on b.imagefileid = c.imagefileid where a.id = ?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,docId);
        if(rs.next()){
            int imageFieldId = rs.getInt("imagefileid");
            String imageName = Util.null2String(rs.getString("imagefilename"));
            String path = Util.null2String(rs.getString("filerealpath"));
            return new ImageEntity(docId,imageFieldId,imageName,path);
        }

        return null;
    }
}
