package com.shop.service;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;


/*상품 이미지를 업로드하고, 상품 이미지 정보를 저장하는 ItemImgService 클래스를 service 패키지에 생성 함*/
@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
    /*@Value 를 통해 properties파일에 등록한 itemimgLocation 값을 불러와서 itemImgLocation 변수에 넣어 준다
    * itemImgFile.getBytes : 사용자가 상품에 이미지를 등록했다면 저장할 경로와 파일의 이름, 파일을 파일의 바이트 배열을 파일 업로드
    * 파라미터로 uploadFile 메소드를 호출한다. 호출 결과 로컬에 저장된 파일의 이름을 imgName 변수에 저장한다.
    * imgUrl = 저장한 상품 이미지를 불러올 경로를 설정한다. 외부 리소스를 불러오는 urlPatterns로 WebMvcConfig클래스에
    * "/imges/**를 설정한다. 또 application,properties에서 설정한 uploadPath프로퍼티 경로인 c:/shop/ 아래 item 폴더에 이미지를
    * 저장하므로 상품 이미지를 불러오는 경로를 붙여준다
    * 상품 이미지 정보 저장으로 정보를 저장한다.*/


    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile)
        throws Exception{
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if (!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName,
                    itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
        /*상품 이미지를 수정한 경우 상품 이미지를 업데이터하고 상품 이미지 아이디를 이용해 기존에 저장했던 상품 이미지 엔티티를 조회한다.*/
        if(!itemImgFile.isEmpty()){
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);
            /*기존에 등록된 상품 이미지 파일이 있을 경우 해당 파일을 삭제한다.*/
            //기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            /*업데이트한 상품 이미지 파일을 업로드한다.*/
            String imgName = fileService.uploadFile(itemImgLocation,oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/"+imgName;
            /*변경된 상품 이미지 정보를 세팅해준다. 여기서 중요한 점은 상품 등록 때처럼 itemImgRepository.save()로직을 호출하지 않는
            * 다는 점이다. savedlteming 엔티티는 현재 영속 상태이므로 데이터를 변경하는 것만으로 변경 감지기능이 동작하여 트랜잭션이 끝날
            * 때까지 update 쿼리가 실행된다. 여기서 중요한 것은 엔티티가 영속성 상태를 유지해야 한다는 것이다.*/
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }
}
