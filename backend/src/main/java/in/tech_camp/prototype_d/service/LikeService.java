package in.tech_camp.prototype_d.service;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.prototype_d.dto.PrototypeDto;
import in.tech_camp.prototype_d.dto.PrototypeListDto;
import in.tech_camp.prototype_d.entity.PrototypeEntity;
import in.tech_camp.prototype_d.repository.LikeRepository;
import in.tech_camp.prototype_d.repository.PrototypeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {


  private final LikeRepository likeRepository;

  private final PrototypeRepository prototypeRepository;

  

  //投稿にいいね追加
  @Transactional
  public void toggleLike (Long prototypeId,Long userId){

  //投稿があるか確認
  PrototypeEntity prototype = prototypeRepository.findById(prototypeId);

  if (prototype == null) {
    throw new IllegalArgumentException("指定された投稿が見つかりません");
  }
  //すでにいいねされている場合は削除、されていない場合は追加
  if (likeRepository.existLikes(userId, prototypeId)) {
    likeRepository.deleteLikes(userId, prototypeId);
  } else {
    likeRepository.insertLikes(userId, prototypeId);
  }
  }

  //いいね順
  @Transactional(readOnly = true)
  public List<PrototypeListDto> getPrototypeOrderByLikes () {
  return likeRepository.orderByLikes();
}
  
}


