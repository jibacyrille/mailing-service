package com.paygarde.mailing.repository;

import com.paygarde.mailing.results.ResultDto;

import java.io.IOException;

public interface EmailRepositoryInterface {
    void addSuccess(ResultDto emailResultDto) throws IOException;
    void addErrors(ResultDto emailResultDto) throws IOException;
}
