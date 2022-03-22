package com.anabada.anabada_api.controller;

import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.dto.MessageDTO;
import com.anabada.anabada_api.dto.PageReportDTO;
import com.anabada.anabada_api.dto.ReportDTO;
import com.anabada.anabada_api.dto.ValidationGroups;
import com.anabada.anabada_api.dto.item.ItemDTO;
import com.anabada.anabada_api.repository.ReportRepository;
import com.anabada.anabada_api.service.item.ItemFindService;
import com.anabada.anabada_api.service.report.ReportFindService;
import com.anabada.anabada_api.service.report.ReportUpdateService;
import com.anabada.anabada_api.service.user.UserFindService;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.util.List;

@Controller
@RequestMapping("/api")
public class ReportController {


    public ReportController(ReportFindService reportFindService, ReportUpdateService reportUpdateService, ItemFindService itemFindService) {
        this.reportFindService = reportFindService;
        this.reportUpdateService = reportUpdateService;
        this.itemFindService = itemFindService;
    }

    ReportFindService reportFindService;
    ReportUpdateService reportUpdateService;
    ItemFindService itemFindService;


    // 신고 조회 api
    //신고 단일 조회
    @GetMapping("/item/reports/{report-idx}")
    public ResponseEntity<ReportDTO> getReportByIdx(@PathVariable(value = "report-idx") Long idx) throws NotFoundException {
        ReportDTO dto = reportFindService.findByIdxDTO(idx);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //신고 전체 조회 - 관리자 
    @GetMapping("/item/reports")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PageReportDTO> getAllReports(@PageableDefault(size = 10, sort = "idx", direction = Sort.Direction.DESC) Pageable pageable) {
        PageReportDTO page = reportFindService.findAll(pageable);

        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    //state 상태에 따른 조회

    //item-idx에 따른 조회
    @GetMapping("items/{item-idx}/reports")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PageReportDTO> getReportByItem(
            @PathVariable(value = "item-idx") Long idx,
            @PageableDefault(size = 10, sort = "idx", direction = Sort.Direction.DESC) Pageable pageable
    ) throws NotFoundException {
        ItemVO item = itemFindService.findByIdx(idx);
        PageReportDTO page = reportFindService.findByItem(item, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);

    }
    //내 신고 조회(유저)
    @GetMapping("/user/reports")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<PageReportDTO> getMyReports(
            @PageableDefault(size = 10,sort = "idx",direction = Sort.Direction.DESC)Pageable pageable)throws
            AuthException{
        PageReportDTO page=reportFindService.findAllWithAuth(pageable);
        return new ResponseEntity<>(page,HttpStatus.OK);
    }


    // 신고 저장 api (유저)
    @PostMapping("/items/{item-idx}/reports")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ReportDTO> saveReport(
            @PathVariable(value = "item-idx") Long itemIdx,
            @RequestBody(required = true) @Validated(ValidationGroups.reportSaveGroup.class) ReportDTO reportDTO
    ) throws AuthException, NotFoundException {

        ReportDTO saveReport = reportUpdateService.save(itemIdx, reportDTO);

        return new ResponseEntity<>(saveReport, HttpStatus.CREATED);
    }

    @PutMapping("/items/reports/{report-idx}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ReportDTO> modifyReport(
            @PathVariable(value = "report-idx") Long idx,
            @RequestParam(value = "state") Long _state) throws AuthException, NotFoundException {
        ReportDTO updatedReport = reportUpdateService.update(idx, reportDTO);
        return new ResponseEntity<>(updatedReport, HttpStatus.OK);
    }


    @DeleteMapping("items/reports/{report-idx}")
    public ResponseEntity<MessageDTO> deleteReport(
            @PathVariable(value = "report-idx") Long idx) throws NotFoundException {
        reportUpdateService.delete(idx);
        return new ResponseEntity<>(new MessageDTO("report deleted"), HttpStatus.NO_CONTENT);

    }


}
