package in.tech_camp.prototype_d.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.prototype_d.custom_user.CustomUserDetail;
import in.tech_camp.prototype_d.dto.PrototypeDto;
import in.tech_camp.prototype_d.dto.PrototypeListDto;
import in.tech_camp.prototype_d.dto.PrototypeStatusDto;
import in.tech_camp.prototype_d.dto.UserDto;
import in.tech_camp.prototype_d.entity.PrototypeEntity;
import in.tech_camp.prototype_d.entity.UserEntity;
import in.tech_camp.prototype_d.form.PrototypeForm;
import in.tech_camp.prototype_d.repository.PrototypeRepository;
import in.tech_camp.prototype_d.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrototypeService {

  private final PrototypeRepository prototypeRepository;
  private final UserRepository userRepository;

  // 全件取得
  public List<PrototypeListDto> getPrototypes() {
    return prototypeRepository.findAll();
  }

  // ユーザー詳細で投稿一覧を取得
  public List<PrototypeListDto> getPrototypesByUserId(Long userId) {
    return prototypeRepository.findByUserId(userId);
  }

  // プロトタイプ詳細
  public PrototypeDto getPrototypeById(Long id) {
    PrototypeEntity entity = prototypeRepository.findById(id);
    PrototypeDto dto = new PrototypeDto();
    if(entity != null){
      dto.setId(entity.getId());
      dto.setTitle(entity.getTitle());
      dto.setCatchcopy(entity.getCatchcopy());
      dto.setConcept(entity.getConcept());
      dto.setImage(entity.getImage());

      UserDto userDto = new UserDto();
      
      // entity.getUserId()を使ってDBからユーザー情報を取得する
      UserEntity user = userRepository.findById(entity.getUserId());
      userDto.setUsername(user.getUsername());
      userDto.setId(user.getId());
      dto.setUser(userDto);
    }else{
      dto = null;
    }
    return dto;
  }

  // プロトタイプ削除機能
  @Transactional
  public void deletePrototype(Long prototypeId, Long currentUserId) {
    Long ownerId = prototypeRepository.findUserIdById(prototypeId);

    if (ownerId == null) {
        throw new IllegalArgumentException("対象のプロトタイプが見つかりません");
    }

    if (!ownerId.equals(currentUserId)) {
        throw new IllegalArgumentException("削除する権限がありません");
    }

    prototypeRepository.deletePrototype(prototypeId);
  }

  //編集用データ
    public PrototypeDto getPrototypeForEdit(Long currentUserId, Long prototypeId) {

      Long ownerId = prototypeRepository.findUserIdById(prototypeId);

      if (ownerId == null){
        throw new IllegalArgumentException("対象のプロトタイプが見つかりません");
      }

      if (!ownerId.equals(currentUserId)) {
        throw new IllegalArgumentException("編集権限がありません");
      }
        PrototypeEntity entity = prototypeRepository.findById(prototypeId);
        
        if (entity == null) {
            return null;
        }

       UserDto userDto = new UserDto();
      
      // entity.getUserId()を使ってDBからユーザー情報を取得する
      UserEntity user = userRepository.findById(entity.getUserId());
      userDto.setUsername(user.getUsername());
      userDto.setId(user.getId());

        PrototypeDto dto = new PrototypeDto();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setCatchcopy(entity.getCatchcopy());
        dto.setConcept(entity.getConcept());
        dto.setImage(entity.getImage());

         dto.setUser(userDto);

        return dto;
    }

    //更新処理
    public void updatePrototype(Long prototypeId, PrototypeDto dto, MultipartFile imageFile,Long currentUserId) {

      Long ownerId = prototypeRepository.findUserIdById(prototypeId);

      if (ownerId == null){
        throw new IllegalArgumentException("対象のプロトタイプが見つかりません");
      }

      if (!ownerId.equals(currentUserId)) {
        throw new IllegalArgumentException("編集権限がありません");
      }

        PrototypeEntity entity = new PrototypeEntity();
        
        entity.setId(prototypeId);
        entity.setTitle(dto.getTitle());
        entity.setCatchcopy(dto.getCatchcopy());
        entity.setConcept(dto.getConcept());
        
        if (imageFile != null && !imageFile.isEmpty()) {
          try {
                // 保存先フォルダ（/uploads）を作成
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                // 重複防止用ファイル名（例: uuid_sample.png）
                String savedFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

                // ファイルを保存
                imageFile.transferTo(new File(uploadDir + savedFileName));

                // 保存したファイル名を entity にセット
                entity.setImage(savedFileName);

            } catch (IOException e) {
                throw new RuntimeException("画像の保存に失敗しました", e);
            }
        } else {
            // 新しい画像が送られてこなかった場合は既存の画像名を保持
            if (dto.getImage() != null && !dto.getImage().trim().isEmpty()) {
            entity.setImage(dto.getImage());
        }
      }


        prototypeRepository.update(entity);

    
}

// 新規追加：保存処理
    @Transactional
    public void insert(PrototypeEntity prototypeEntity) {
        prototypeRepository.insert(prototypeEntity);
    }

  // プロトタイプランダム取得
  public Map<String, Object> getPrototypeToday(CustomUserDetail currentuser){
    // 現在あるIdをリストとして取得
    List<Long> prototypeIds= prototypeRepository.findAllId();
    // 今日の日付をパラメーターとして使用
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    String dateString = today.format(formatter);
    int dateNumber = Integer.parseInt(dateString);
    // 検索のidとする数字を決定
    long calculatedValue = (long) dateNumber * dateNumber + currentuser.getId();
    int index = (int) (Math.abs(calculatedValue) % prototypeIds.size());
    Long randomNum = prototypeIds.get(index);
    // PrototypeのDTOを取得
    PrototypeDto dto = getPrototypeById(randomNum);

    // 今日の運勢を決める数字を算出
    Integer luck = (int) (Math.abs(calculatedValue) % 6);
    System.out.println("calculatedValue"+calculatedValue);
    System.out.println(prototypeIds.size());

    // HashMapに入れる
    Map<String, Object> responseData = new HashMap<>();
        responseData.put("prototype", dto);
        responseData.put("luck", luck);
    return responseData;
  }
    /**
     * プロトタイプの登録処理（画像保存 ＋ DB登録）
     */
    @Transactional
    public PrototypeEntity createPrototype(PrototypeForm form, MultipartFile imageFile, Long userId) throws IOException {
        // 1. 画像ファイルをローカルストレージへ保存
        String fileName = saveImageFile(imageFile);

        // 2. Entityの作成と値のセット
        PrototypeEntity entity = new PrototypeEntity();
        entity.setTitle(form.getTitle());
        entity.setCatchcopy(form.getCatchcopy());
        entity.setConcept(form.getConcept());
        entity.setImage(fileName);
        entity.setUserId(userId);

        // 3. DBへ保存（既存のinsert/save処理）
        prototypeRepository.insert(entity);

        return entity;
    }

    /**
     * 画像ファイルの保存処理（プライベートヘルパーメソッド）
     */
    private String saveImageFile(MultipartFile imageFile) throws IOException {
        String uploadDir = Paths.get(System.getProperty("user.dir"), "uploads").toAbsolutePath().toString();
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalFilename = imageFile.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + extension;

        File dest = new File(dir, fileName);
        imageFile.transferTo(dest);

        return fileName;
    }

    public PrototypeStatusDto getPrototypeRandom() {
      List<Long> prototypeIds= prototypeRepository.findAllId();
      Random random = new Random();
      int randomInt = random.nextInt(prototypeIds.size());
      Long index = prototypeIds.get(randomInt);
      PrototypeDto prototypeDto = getPrototypeById(index);
      PrototypeStatusDto dto = new PrototypeStatusDto();

      dto.setId(prototypeDto.getId());
      dto.setTitle(prototypeDto.getTitle());
      dto.setCatchcopy(prototypeDto.getCatchcopy());
      dto.setImage(prototypeDto.getImage());
      dto.setUserId(prototypeDto.getUser().getId());
      dto.setUsername(prototypeDto.getUser().getUsername());


      // レアリティを算出
      String baseName = prototypeDto.getImage().replaceFirst("[.][^.]+$", "");
      Long score;
      try{
        long seed;
        // 内部の64bit数値を合成してシードにする
        UUID uuid = UUID.fromString(baseName);
        seed = uuid.getMostSignificantBits() ^ uuid.getLeastSignificantBits();
        // シード値を元に乱数生成器を作成
        Random generator = new Random(seed);
        // 1〜10000の数値を生成
        score = (long) (generator.nextInt(10000) + 1);
      }catch(Exception e){
        score = 1L;
      }
        dto.setRarity(score);
        //↓ここはいいね数などによって調整
        dto.setAttack(score * prototypeDto.getId() / 10);
        dto.setDefense(score * prototypeDto.getId());
      return dto;
    }
}
