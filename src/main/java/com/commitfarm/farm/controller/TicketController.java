package com.commitfarm.farm.controller;

import com.commitfarm.farm.dto.ticket.request.CreateTicketDto;
import com.commitfarm.farm.dto.ticket.request.UpdateStatusReq;
import com.commitfarm.farm.dto.ticket.response.*;
import com.commitfarm.farm.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "티켓 API", description = "티켓 관련 API")// grouping by tag name
@RestController
@RequestMapping(value = "/api")
public class TicketController {

    @Autowired
    private TicketService ticketService;
// ticket crud

    @PostMapping("/create/ticket/{projectId}")
    @Operation(summary =  "티켓 생성 ", description = "projectId로 프로젝트 내 티켓 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "생성됨 메세지"
                    )
            }
    )
    public void createTicket(@PathVariable Long projectId, @RequestBody CreateTicketDto ticketDTO) {
         ticketService.createTicket(projectId, ticketDTO);
    }

    @GetMapping("/read/ticket-list/{projectId}/{userId}")
    @Operation(summary = "프로젝트 내, 내 티켓 리스트", description = "projectId로 프로젝트 내 티켓 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 내 티켓 리스트를 반환함."
                    )
            }
    )
    public TicketListRes readTicketList(@PathVariable Long projectId, @PathVariable Long userId) {
        return ticketService.readTicketList(projectId, userId);
    }


    @GetMapping("/read/assigned/ticket-list/{projectId}/{userId}")
    @Operation(summary = "프로젝트 내, 내 Assigned티켓 리스트1", description = "projectId로 프로젝트 내 티켓 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 내 티켓 리스트를 반환함."
                    )
            }
    )
    public List<AssignedTicketListRes> readAssignedTicketList(@PathVariable Long projectId, @PathVariable Long userId) {
        return ticketService.readAssignedTicketList(projectId, userId);
    }

    @GetMapping("/read/new/ticket-list/{projectId}/{userId}")
    @Operation(summary = "프로젝트 내, 내 new 티켓 리스트2", description = "projectId로 프로젝트 내 티켓 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 내 티켓 리스트를 반환함."
                    )
            }
    )
    public List<NewTicketListRes> readNewTicketList(@PathVariable Long projectId, @PathVariable Long userId) {
        return ticketService.readNewTicketList(projectId, userId);
    }


    @GetMapping("/read/resolved/ticket-list/{projectId}/{userId}")
    @Operation(summary = "프로젝트 내, 내 resolved 티켓 리스트3", description = "projectId로 프로젝트 내 티켓 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 내 티켓 리스트를 반환함."
                    )
            }
    )
    public List<ResolvedTicketListRes> readResolvedTicketList(@PathVariable Long projectId, @PathVariable Long userId) {
        return ticketService.readResolvedTicketList(projectId, userId);
    }

    @GetMapping("/read/reopen/ticket-list/{projectId}/{userId}")
    @Operation(summary = "프로젝트 내, 내 reopen 티켓 리스트4", description = "projectId로 프로젝트 내 티켓 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 내 티켓 리스트를 반환함."
                    )
            }
    )
    public List<ReopenedTicketListRes> readReopenedTicketList(@PathVariable Long projectId, @PathVariable Long userId) {
        return ticketService.readReopenedTicketList(projectId, userId);
    }

    @GetMapping("/read/closed/ticket-list/{projectId}/{userId}")
    @Operation(summary = "프로젝트 내, 내 closed 티켓 리스트5", description = "projectId로 프로젝트 내 티켓 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 내 티켓 리스트를 반환함."
                    )
            }
    )
    public List<ClosedTicketListRes> readClosedTicketList(@PathVariable Long projectId, @PathVariable Long userId) {
        return ticketService.readClosedTicketList(projectId, userId);
    }




    @GetMapping("/read/all/ticket/{ticketId}")
    @Operation(summary =  "티켓 리스트 정보 ", description = "ticketId로  티켓 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 내 티켓 상세 정보: 댓글등을 반환함."
                    )
            }
    )
    public DetailTicketRes readTicket(@PathVariable Long ticketId) {
         return ticketService.readDetailTicket(ticketId);
    }


    @GetMapping("/read/ticket/statistics/{projectId}")
    @Operation(summary = "티켓 status 통계", description = "projectId로 프로젝트 내 티켓 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "이번 달 생성된 티켓의 Status (day/month)통계 정보를 반환함."
                    )
            }
    )
    public StaticsRes readTicketStatics(@PathVariable Long projectId) {
        return ticketService.readTicketStatics(projectId);
    }


    @PutMapping("/update/ticket/status/{projectId}/{ticketId}/{userId}")
    @Operation(summary = "U:티켓 업데이트 :status 변경", description = "ticketId로 티켓 status 변경",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "status 변경 성공"
                    )
            }
    )
    public String updateTicketStatus(@PathVariable Long projectId, @PathVariable Long ticketId, @PathVariable Long userId, @RequestBody UpdateStatusReq dto) {
        return ticketService.updateTicketStatus(projectId, ticketId, userId, dto);
    }




    @DeleteMapping("/delete/{ticketId}")//
    public void deleteTicket(@PathVariable  Long ticketId){
        ticketService.deleteTicket( ticketId);

    }







    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }



}
