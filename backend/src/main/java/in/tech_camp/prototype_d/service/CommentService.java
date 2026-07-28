package in.tech_camp.prototype_d.service;

import in.tech_camp.prototype_d.dto.CommentDto;
import in.tech_camp.prototype_d.entity.CommentEntity;
import in.tech_camp.prototype_d.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;

  // コメント一覧を取得
  @Transactional(readOnly = true)
  public List<CommentDto> getCommentsByPrototypeId(Long prototypeId) {
      return commentRepository.findByPrototypeId(prototypeId);
  }
  
  // DBに保存する
  @Transactional
  public void saveComment(Long prototypeId, Long userId, String commentText) {
      CommentEntity commentEntity = new CommentEntity();
      commentEntity.setPrototypeId(prototypeId);
      commentEntity.setUserId(userId);
      commentEntity.setComment(commentText);

      commentRepository.save(commentEntity);
  }
}