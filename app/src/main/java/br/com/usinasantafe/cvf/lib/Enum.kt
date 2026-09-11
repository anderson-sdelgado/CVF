package br.com.usinasantafe.cvf.lib

enum class StatusSend { STARTED, SEND, SENT }
enum class Errors { FIELD_EMPTY, TOKEN, UPDATE, EXCEPTION, INVALID, NOT_SELECTION, PASSWORD_INVALID, RETURN_INVALID_FRONT, CART_REPEATED, INVERTED_CART }
enum class TypeButton { NUMERIC, CLEAN, OK, CANCEL }

enum class LevelUpdate { RECOVERY, CLEAN, SAVE, GET_TOKEN, SAVE_TOKEN, FINISH_UPDATE_INITIAL, FINISH_UPDATE_COMPLETED, CHECK_DATA }

enum class OptionMenu { DELETE, CONFIG, FRONT, RELEASE, CLOSE }
enum class OptionReturn { DRIVER, TRUCK, CART, REVIEW }

enum class Option { INSERT, EDIT }
enum class TypeTruck { TRUCK, HAULAGE_TRUCK }
enum class TypeEquip { TRUCK, CART }
enum class FlowCart { NORMAL, RETURN }
enum class FlowApp { CONFIG, FRONT, RELEASE, NOTE }