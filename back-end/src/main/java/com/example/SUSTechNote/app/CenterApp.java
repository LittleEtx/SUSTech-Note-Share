package com.example.SUSTechNote.app;

import com.example.SUSTechNote.entity.Notebook;
import com.example.SUSTechNote.interfaces.NotebookInterface;
import com.example.SUSTechNote.service.NotebookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/center")
public class CenterApp {
    private final Logger logger = LoggerFactory.getLogger(CenterApp.class);
    private final NotebookService notebookService;

    public CenterApp(NotebookService notebookService) {
        this.notebookService = notebookService;
    }

    @GetMapping("get-notebooks")
    public ResponseEntity<?> findNotebooks(){
        try {
            List<Notebook> notebooks = notebookService.findNotebooks();
            return ResponseEntity.ok(NotebookInterface.fromNotebooks(notebooks));
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return ResponseEntity.badRequest().body("Notebook query failed \n" + e);
        }
    }

    @GetMapping("get-public-notebooks")
    public ResponseEntity<?> findNotebookByNotebookID(@Param("userID") int userID){
        try {
            List<Notebook> notebooks = notebookService.findPublicNotebooks(userID);
            return ResponseEntity.ok(NotebookInterface.fromNotebooks(notebooks));
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return ResponseEntity.badRequest().body("Notebook query failed \n" + e);
        }
    }
}
