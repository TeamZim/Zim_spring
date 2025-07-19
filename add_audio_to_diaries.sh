#!/bin/bash

# 랜덤으로 선택된 다이어리 ID들
diary_ids=(18 4 1 22 26 5 31 8 14 23 7 16 21 33 32 11 28 2 13 17)

# 실제 업로드된 오디오 URL들
audio_urls=(
"https://me-mory.mooo.com/api/files?key=audio/2e3f7cf9-22df-4e3c-b6b6-a7115ba6b2ad_audio1.m4a"
"https://me-mory.mooo.com/api/files?key=audio/2043542e-41ea-47d7-acea-8f6f5235d5e3_audio2.m4a"
"https://me-mory.mooo.com/api/files?key=audio/01e323da-0dfe-469c-8f90-a1cf1567b3fd_audio3.m4a"
"https://me-mory.mooo.com/api/files?key=audio/78a84c00-86f3-435f-b425-571840f8c436_audio4.m4a"
"https://me-mory.mooo.com/api/files?key=audio/c26461fc-5efe-4b68-aacb-daa246197477_audio5.m4a"
"https://me-mory.mooo.com/api/files?key=audio/38e985bf-7ca6-4b2d-8125-da23c641b458_audio6.m4a"
"https://me-mory.mooo.com/api/files?key=audio/c0a5b29e-1121-456d-a83f-27ff0f4b7dd9_audio7.m4a"
"https://me-mory.mooo.com/api/files?key=audio/8a570dd8-bb81-4273-973e-1116bd2672a6_audio8.m4a"
"https://me-mory.mooo.com/api/files?key=audio/5bc53648-7261-4577-83a1-326d7ef34d5b_audio9.m4a"
"https://me-mory.mooo.com/api/files?key=audio/8046f0f7-8bcb-41a3-b402-edba93a56713_audio10.m4a"
"https://me-mory.mooo.com/api/files?key=audio/a991ca68-63f6-4e96-b3cc-d189144a09f4_audio11.m4a"
"https://me-mory.mooo.com/api/files?key=audio/0af79ee9-c051-4d7f-b853-e427900f4ae9_audio12.m4a"
"https://me-mory.mooo.com/api/files?key=audio/232cfd28-4fe3-476f-b6b9-a2302cfee5b3_audio13.m4a"
"https://me-mory.mooo.com/api/files?key=audio/03b84c9e-f3e4-46f9-a81d-744ed9b8cd5b_audio14.m4a"
"https://me-mory.mooo.com/api/files?key=audio/35a7a2d0-45d2-4bad-9390-e832402f6142_audio15.m4a"
"https://me-mory.mooo.com/api/files?key=audio/b1f0277d-4147-40b9-a2d8-1d0c6190ced3_audio16.m4a"
"https://me-mory.mooo.com/api/files?key=audio/4e30c01d-cfa7-4ab7-9b1f-fa239480c1d5_audio17.m4a"
"https://me-mory.mooo.com/api/files?key=audio/a9952197-2c49-4c5d-bda8-6320b32d7e3e_audio18.m4a"
"https://me-mory.mooo.com/api/files?key=audio/2cf7f728-7241-4a14-a94f-24cc27c88318_audio19.m4a"
"https://me-mory.mooo.com/api/files?key=audio/ed696d07-df16-4536-b7c8-05672aa08448_audio20.m4a"
)

echo "랜덤 다이어리 20개에 실제 오디오 URL 추가 시작..."

for i in "${!diary_ids[@]}"; do
    diary_id=${diary_ids[$i]}
    audio_url=${audio_urls[$i]}
    
    echo "다이어리 ID $diary_id 에 오디오 추가 중..."
    
    curl -X PUT "https://me-mory.mooo.com/api/diaries/$diary_id/optional-fields" \
      -H "Content-Type: application/json" \
      -d "{\"audioUrl\": \"$audio_url\"}" \
      -s
    
    if [ $? -eq 0 ]; then
        echo "✓ 다이어리 ID $diary_id 완료"
    else
        echo "✗ 다이어리 ID $diary_id 실패"
    fi
    
    sleep 0.5
done

echo "=========================================="
echo "모든 다이어리에 실제 오디오 URL 추가 완료!"
echo "오디오가 추가된 다이어리 ID들:"
echo "${diary_ids[@]}"
