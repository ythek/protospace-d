package in.tech_camp.prototype_d.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.prototype_d.entity.CommentEntity;
import in.tech_camp.prototype_d.entity.PrototypeEntity;
import in.tech_camp.prototype_d.entity.UserEntity;
import in.tech_camp.prototype_d.repository.CommentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    // コメント一覧を取得
    @Transactional(readOnly = true)
    public List<CommentEntity> getCommentsByPrototypeId(Long prototypeId) {
        return commentRepository.findByPrototypeId(prototypeId);
    }
    
    // DBに保存する
    @Transactional
    public void saveComment(Long prototypeId, Long userId, String commentText) {
        // ネストする UserEntity と PrototypeEntity を生成して ID をセット
        UserEntity user = new UserEntity();
        user.setId(userId);

        PrototypeEntity prototype = new PrototypeEntity();
        prototype.setId(prototypeId);

        // CommentEntity の組み立て
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setUser(user);
        commentEntity.setPrototype(prototype);
        commentEntity.setComment(commentText);

        // Repository の insert メソッドを実行
        commentRepository.insert(commentEntity);
    }
}