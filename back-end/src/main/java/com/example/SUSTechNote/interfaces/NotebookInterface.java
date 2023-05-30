package com.example.SUSTechNote.interfaces;

import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.Notebook;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public record NotebookInterface(
    String notebookID,
    int authorID,
    String title,
    String tag,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updateTime,
    String cover,
    String description,
    String directory,
    int isPublic,
    int likeCount,
    int starCount

){
    public static NotebookInterface fromNotebook(Notebook notebook){
        return new NotebookInterface(
            notebook.getNotebookID(),
            notebook.getAuthorID(),
            notebook.getNotebookName(),
            notebook.getTag(),
            notebook.getUpdateTime(),
            notebook.getCover(),
            notebook.getDescription(),
            notebook.getDirectory(),
            notebook.getIsPublic(),
            notebook.getLikeNum() == null ? 0 : notebook.getLikeNum(),
            notebook.getStar() == null ? 0 : notebook.getStar()
        );
    }
    public static List<NotebookInterface> fromNotebooks(List<Notebook> notebooks){
        return notebooks.stream().map(NotebookInterface::fromNotebook).toList();
    }

    public static NotebookInterface fromNotebook(JSONObject notebook){
        return new NotebookInterface(
                (String) notebook.get("notebookid"),
                (int) notebook.get("authorid"),
                (String) notebook.get("notebook_name"),
                (String) notebook.get("tag"),
                ((Timestamp) notebook.get("update_time")).toLocalDateTime(),
                (String) notebook.get("cover"),
                (String) notebook.get("description"),
                (String) notebook.get("directory"),
                (Integer) notebook.get("is_public"),
                (int) notebook.get("like_num"),
                (int) notebook.get("star")
        );
    }

    public static List<NotebookInterface> fromNotebookMap(Collection<JSONObject> notebooks){
        return notebooks.stream().map(NotebookInterface::fromNotebook).toList();
    }
}
