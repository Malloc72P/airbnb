//
//  MockJSON.swift
//  AirbnbApp
//
//  Created by Song on 2021/05/20.
//

import Foundation

struct MockJSON {
    static let bestDestinations = """
    [
        {
            "name": "서울",
            "driving_time": "차로 30분 거리"
        },
        {
            "name": "광주",
            "driving_time": "차로 4시간 거리"
        },
        {
            "name": "의정부시",
            "driving_time": "차로 30분 거리"
        },
        {
            "name": "수원시",
            "driving_time": "차로 45분 거리"
        },
        {
            "name": "대구",
            "driving_time": "차로 3.5시간 거리"
        },
        {
            "name": "울산",
            "driving_time": "차로 4.5시간 거리"
        },
        {
            "name": "대전",
            "driving_time": "차로 2시간 거리"
        },
        {
            "name": "부천시",
            "driving_time": "차로 30분 거리"
        },
    ]
    """.data(using: .utf8)!
}
