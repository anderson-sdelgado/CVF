package br.com.usinasantafe.cvf.lib

enum class StatusSend { STARTED, SEND, SENT }
enum class Errors { FIELD_EMPTY, TOKEN, UPDATE, EXCEPTION, INVALID }
enum class TypeButton { NUMERIC, CLEAN, OK, UPDATE }

enum class LevelUpdate { RECOVERY, CLEAN, SAVE, GET_TOKEN, SAVE_TOKEN, FINISH_UPDATE_INITIAL, FINISH_UPDATE_COMPLETED }
