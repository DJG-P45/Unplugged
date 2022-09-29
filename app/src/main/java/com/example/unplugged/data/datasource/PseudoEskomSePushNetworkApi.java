package com.example.unplugged.data.datasource;

import org.json.JSONException;
import org.json.JSONObject;

public class PseudoEskomSePushNetworkApi {

    public void getStatus(IApiCallback callback) {
        try {
            JSONObject result = new JSONObject("{\n" +
                    "    \"status\": {\n" +
                    "        \"capetown\": {\n" +
                    "            \"name\": \"Cape Town\",\n" +
                    "            \"next_stages\": [\n" +
                    "                {\n" +
                    "                    \"stage\": \"1\",\n" +
                    "                    \"stage_start_timestamp\": \"2022-08-08T17:00:00+02:00\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"stage\": \"0\",\n" +
                    "                    \"stage_start_timestamp\": \"2022-08-08T22:00:00+02:00\"\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"stage\": \"0\",\n" +
                    "            \"stage_updated\": \"2022-08-08T00:08:16.837063+02:00\"\n" +
                    "        },\n" +
                    "        \"eskom\": {\n" +
                    "            \"name\": \"National\",\n" +
                    "            \"next_stages\": [\n" +
                    "                {\n" +
                    "                    \"stage\": \"2\",\n" +
                    "                    \"stage_start_timestamp\": \"2022-08-08T16:00:00+02:00\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"stage\": \"0\",\n" +
                    "                    \"stage_start_timestamp\": \"2022-08-09T00:00:00+02:00\"\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"stage\": \"1\",\n" +
                    "            \"stage_updated\": \"2022-08-08T16:12:53.725852+02:00\"\n" +
                    "        }\n" +
                    "    }\n" +
                    "}");

            callback.onResponse(result.getJSONObject("status").getJSONObject("eskom").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAreaInfo(String id, IApiCallback callback) {
        try {
            JSONObject result = new JSONObject("{\n" +
                    "    \"events\": [\n" +
                    "        {\n" +
                    "            \"end\": \"2022-08-08T22:30:00+02:00\",\n" +
                    "            \"note\": \"Stage 2\",\n" +
                    "            \"start\": \"2022-08-08T20:00:00+02:00\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"info\": {\n" +
                    "        \"name\": \"Sandton-WEST (4)\",\n" +
                    "        \"region\": \"Eskom Direct, City of Johannesburg, Gauteng\"\n" +
                    "    },\n" +
                    "    \"schedule\": {\n" +
                    "        \"days\": [\n" +
                    "            {\n" +
                    "                \"date\": \"2022-09-22\",\n" +
                    "                \"name\": \"Monday\",\n" +
                    "                \"stages\": [\n" +
                    "                    [],\n" +
                    "                    [\n" +
                    "                        \"20:00-22:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"12:00-14:30\",\n" +
                    "                        \"20:00-22:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"04:00-06:30\",\n" +
                    "                        \"12:00-14:30\",\n" +
                    "                        \"20:00-22:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"04:00-06:30\",\n" +
                    "                        \"12:00-14:30\",\n" +
                    "                        \"20:00-22:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"04:00-06:30\",\n" +
                    "                        \"12:00-14:30\",\n" +
                    "                        \"20:00-00:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"04:00-06:30\",\n" +
                    "                        \"12:00-16:30\",\n" +
                    "                        \"20:00-00:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"04:00-08:30\",\n" +
                    "                        \"12:00-16:30\",\n" +
                    "                        \"20:00-00:30\"\n" +
                    "                    ]\n" +
                    "                ]\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"date\": \"2022-09-23\",\n" +
                    "                \"name\": \"Tuesday\",\n" +
                    "                \"stages\": [\n" +
                    "                    [\n" +
                    "                        \"02:00-04:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-04:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-04:30\",\n" +
                    "                        \"18:00-20:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-04:30\",\n" +
                    "                        \"10:00-12:30\",\n" +
                    "                        \"18:00-20:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-06:30\",\n" +
                    "                        \"10:00-12:30\",\n" +
                    "                        \"18:00-20:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-06:30\",\n" +
                    "                        \"10:00-12:30\",\n" +
                    "                        \"18:00-20:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-06:30\",\n" +
                    "                        \"10:00-12:30\",\n" +
                    "                        \"18:00-22:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-06:30\",\n" +
                    "                        \"10:00-14:30\",\n" +
                    "                        \"18:00-22:30\"\n" +
                    "                    ]\n" +
                    "                ]\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"date\": \"2022-09-24\",\n" +
                    "                \"name\": \"Wednesday\",\n" +
                    "                \"stages\": [\n" +
                    "                    [\n" +
                    "                        \"10:00-12:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-04:30\",\n" +
                    "                        \"10:00-12:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-04:30\",\n" +
                    "                        \"10:00-12:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-04:30\",\n" +
                    "                        \"10:00-12:30\",\n" +
                    "                        \"18:00-20:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-04:30\",\n" +
                    "                        \"10:00-14:30\",\n" +
                    "                        \"18:00-20:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-06:30\",\n" +
                    "                        \"10:00-14:30\",\n" +
                    "                        \"18:00-20:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-06:30\",\n" +
                    "                        \"10:00-14:30\",\n" +
                    "                        \"18:00-20:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-06:30\",\n" +
                    "                        \"10:00-14:30\",\n" +
                    "                        \"18:00-22:30\"\n" +
                    "                    ]\n" +
                    "                ]\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"date\": \"2022-09-25\",\n" +
                    "                \"name\": \"Thursday\",\n" +
                    "                \"stages\": [\n" +
                    "                    [\n" +
                    "                        \"18:00-20:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"10:00-12:30\",\n" +
                    "                        \"18:00-20:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-04:30\",\n" +
                    "                        \"10:00-12:30\",\n" +
                    "                        \"18:00-20:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-04:30\",\n" +
                    "                        \"10:00-12:30\",\n" +
                    "                        \"18:00-20:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-04:30\",\n" +
                    "                        \"10:00-12:30\",\n" +
                    "                        \"18:00-22:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-04:30\",\n" +
                    "                        \"10:00-14:30\",\n" +
                    "                        \"18:00-22:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-06:30\",\n" +
                    "                        \"10:00-14:30\",\n" +
                    "                        \"18:00-22:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-06:30\",\n" +
                    "                        \"10:00-14:30\",\n" +
                    "                        \"18:00-22:30\"\n" +
                    "                    ]\n" +
                    "                ]\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"date\": \"2022-09-26\",\n" +
                    "                \"name\": \"Friday\",\n" +
                    "                \"stages\": [\n" +
                    "                    [],\n" +
                    "                    [\n" +
                    "                        \"18:00-20:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"10:00-12:30\",\n" +
                    "                        \"18:00-20:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-04:30\",\n" +
                    "                        \"10:00-12:30\",\n" +
                    "                        \"18:00-20:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-04:30\",\n" +
                    "                        \"10:00-12:30\",\n" +
                    "                        \"18:00-20:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-04:30\",\n" +
                    "                        \"10:00-12:30\",\n" +
                    "                        \"18:00-22:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-04:30\",\n" +
                    "                        \"10:00-14:30\",\n" +
                    "                        \"18:00-22:30\"\n" +
                    "                    ],\n" +
                    "                    [\n" +
                    "                        \"02:00-06:30\",\n" +
                    "                        \"10:00-14:30\",\n" +
                    "                        \"18:00-22:30\"\n" +
                    "                    ]\n" +
                    "                ]\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"source\": \"https://loadshedding.eskom.co.za/\"\n" +
                    "    }\n" +
                    "}");
            callback.onResponse(result.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void findAreas(String searchText, IApiCallback callback) {
        try {
            JSONObject result = new JSONObject("{\n" +
                    "    \"areas\": [\n" +
                    "        {\n" +
                    "            \"id\": \"westerncape-2-stellenboschmunicipality\",\n" +
                    "            \"name\": \"Stellenbosch Municipality (2)\",\n" +
                    "            \"region\": \"Western Cape\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": \"westerncape-8-stellenboschfarmers\",\n" +
                    "            \"name\": \"Stellenbosch farmers (8)\",\n" +
                    "            \"region\": \"Western Cape\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": \"eskomdirect-5215-stellenboschpart1outlyingstellenboschwesterncape\",\n" +
                    "            \"name\": \"Stellenbosch Part 1 Outlying\",\n" +
                    "            \"region\": \"Eskom Direct (Web), Stellenbosch, Western Cape\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": \"eskde-4-stellenboschnucityofcapetownwesterncape\",\n" +
                    "            \"name\": \"Stellenbosch NU (4)\",\n" +
                    "            \"region\": \"Eskom Direct, City of Cape Town, Western Cape\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": \"eskde-4-stellenboschnustellenboschwesterncape\",\n" +
                    "            \"name\": \"Stellenbosch NU (4)\",\n" +
                    "            \"region\": \"Eskom Direct, Stellenbosch, Western Cape\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": \"eskde-8-stellenboschnustellenboschwesterncape\",\n" +
                    "            \"name\": \"Stellenbosch NU (8)\",\n" +
                    "            \"region\": \"Eskom Direct, Stellenbosch, Western Cape\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": \"eskde-15-stellenboschnucityofcapetownwesterncape\",\n" +
                    "            \"name\": \"Stellenbosch NU (15)\",\n" +
                    "            \"region\": \"Eskom Direct, City of Cape Town, Western Cape\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": \"eskde-15-stellenboschnustellenboschwesterncape\",\n" +
                    "            \"name\": \"Stellenbosch NU (15)\",\n" +
                    "            \"region\": \"Eskom Direct, Stellenbosch, Western Cape\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": \"eskde-16-stellenboschmohokarefreestate\",\n" +
                    "            \"name\": \"Stellenbosch (16)\",\n" +
                    "            \"region\": \"Eskom Direct, Mohokare, Free State\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": \"eskde-16-stellenboschnustellenboschwesterncape\",\n" +
                    "            \"name\": \"Stellenbosch NU (16)\",\n" +
                    "            \"region\": \"Eskom Direct, Stellenbosch, Western Cape\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": \"eskde-16-stellengiftdrakensteinwesterncape\",\n" +
                    "            \"name\": \"Stellengift (16)\",\n" +
                    "            \"region\": \"Eskom Direct, Drakenstein, Western Cape\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": \"eskdo-5-stellenboschvleibeaufortwestwesterncape\",\n" +
                    "            \"name\": \"Stellenboschvlei (5)\",\n" +
                    "            \"region\": \"Eskom Direct, Beaufort West, Western Cape\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": \"eskdo-9-stellenhofsundaysrivervalleyeasterncape\",\n" +
                    "            \"name\": \"Stellenhof (9)\",\n" +
                    "            \"region\": \"Eskom Direct, Sundays River Valley, Eastern Cape\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": \"westerncape-2-universityofstellenbosch\",\n" +
                    "            \"name\": \"University of Stellenbosch (2)\",\n" +
                    "            \"region\": \"Western Cape\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}");
            callback.onResponse(result.getJSONArray("areas").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
