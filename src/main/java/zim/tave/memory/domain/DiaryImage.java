package zim.tave.memory.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class DiaryImage {

    @Id
    @GeneratedValue
    @Column(name = "DiaryImageId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diaryId")
    private Diary diary;

    @Column(nullable = false)
    private String imageUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CameraType cameraType; // FRONT, BACK
    
    private boolean isRepresentative; // 사용자가 선택한 대표사진
    
    private int imageOrder; // 이미지 순서 (1, 2)
    
    public enum CameraType {
        FRONT,  // 전면 카메라
        BACK    // 후면 카메라
    }
}
