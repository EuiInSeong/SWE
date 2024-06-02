package com.commitfarm.farm.service;

import com.commitfarm.farm.domain.Comment;
import com.commitfarm.farm.domain.Users;
import com.commitfarm.farm.domain.Ticket;
import com.commitfarm.farm.dto.comment.request.CreateCommentDTO;
import com.commitfarm.farm.repository.CommentRepository;
import com.commitfarm.farm.repository.TicketRepository;
import com.commitfarm.farm.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Transactional
    public void createComment(Long usersId, Long ticketId, CreateCommentDTO dto) {
        // leave comment -> only ticket not pro, milestone
        Users users = usersRepository.findById(usersId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + usersId));
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new IllegalArgumentException("해당 티켓이 없습니다. id=" + ticketId));

        Comment comment = new Comment();
        comment.setUser(users);
        comment.setTicket(ticket);
        comment.setContent(dto.getContent());
        commentRepository.save(comment);

    }

    @Transactional
    public void deleteComment(Long userId,Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + commentId));
        Users reporter = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        if(comment.getUser() != reporter){
            throw new IllegalArgumentException("해당 유저는 댓글을 삭제할 권한이 없습니다.");
        }
        commentRepository.deleteById(commentId);
    }





}
