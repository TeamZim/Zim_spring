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

    private String imageUrl;
    private boolean isRepresentative;
}