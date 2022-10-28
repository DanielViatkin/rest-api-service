package com.epam.esm.service.validator.impl;

import com.epam.esm.constant.database.ColumnConstant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ParamsValidator {
    private final List<String> validSortTypes = Arrays.asList("asc", "desc");
    private final Set<String> certificateColumns = ColumnConstant.getCertificateColumnAsSet();
    public boolean isSortParamsValid(List<String> sortParams){
        if (sortParams != null) {
            for(String sortParam : sortParams){
                if(!certificateColumns.contains(sortParam)){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSortTypesValid(List<String> sortTypes){
        if (sortTypes != null){
            for (String sortType : sortTypes){
                boolean isSortTypeValid = validSortTypes.
                        stream().anyMatch(validSortType -> sortType.equalsIgnoreCase(validSortType));
                if(!isSortTypeValid){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSearchParamsValid(String searchParam){
        if(searchParam == null){
            return true;
        }
        return certificateColumns.contains(searchParam);
    }
}
