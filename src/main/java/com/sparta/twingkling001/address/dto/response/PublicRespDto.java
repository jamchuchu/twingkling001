package com.sparta.twingkling001.address.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

public class PublicRespDto {

    @JsonProperty("results")
    private Results results;

    public static class Results {
        private Common common;
        private List<Juso> juso;

        public Common getCommon() {
            return common;
        }

        public void setCommon(Common common) {
            this.common = common;
        }

        public List<Juso> getJuso() {
            return juso;
        }

        public void setJuso(List<Juso> juso) {
            this.juso = juso;
        }

        public void addJuso(PublicRespDto results) {
            this.juso.addAll(results.getResults().getJuso());
        }
    }

    @Getter
    public static class Common {
        @JsonProperty("totalCount")
        private String totalCount;
        @JsonProperty("currentPage")
        private String currentPage;
        @JsonProperty("countPerPage")
        private String countPerPage;
        @JsonProperty("errorCode")
        private String errorCode;
        @JsonProperty("errorMessage")
        private String errorMessage;

        // getters and setters
    }

    @Getter
    public static class Juso {
        @JsonProperty("roadAddr")
        private String roadAddr;
        @JsonProperty("roadAddrPart1")
        private String roadAddrPart1;
        @JsonProperty("roadAddrPart2")
        private String roadAddrPart2;
        @JsonProperty("jibunAddr")
        private String jibunAddr;
        @JsonProperty("engAddr")
        private String engAddr;
        @JsonProperty("zipNo")
        private String zipNo;
        @JsonProperty("admCd")
        private String admCd;
        @JsonProperty("rnMgtSn")
        private String rnMgtSn;
        @JsonProperty("bdMgtSn")
        private String bdMgtSn;
        @JsonProperty("detBdNmList")
        private String detBdNmList;
        @JsonProperty("bdNm")
        private String bdNm;
        @JsonProperty("bdKdcd")
        private String bdKdcd;
        @JsonProperty("siNm")
        private String siNm;
        @JsonProperty("sggNm")
        private String sggNm;
        @JsonProperty("emdNm")
        private String emdNm;
        @JsonProperty("liNm")
        private String liNm;
        @JsonProperty("rn")
        private String rn;
        @JsonProperty("udrtYn")
        private String udrtYn;
        @JsonProperty("buldMnnm")
        private String buldMnnm;
        @JsonProperty("buldSlno")
        private String buldSlno;
        @JsonProperty("mtYn")
        private String mtYn;
        @JsonProperty("lnbrMnnm")
        private String lnbrMnnm;
        @JsonProperty("lnbrSlno")
        private String lnbrSlno;
        @JsonProperty("emdNo")
        private String emdNo;

        // getters and setters
    }

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }
}
