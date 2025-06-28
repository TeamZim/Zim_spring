package zim.tave.memory.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonCreator;

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
        FRONT,
        BACK;

        @JsonCreator
        public static CameraType from(String value) {
            return CameraType.valueOf(value.toUpperCase());
        }
    }

}
