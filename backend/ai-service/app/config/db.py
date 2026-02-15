from functools import lru_cache

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, declarative_base

from app.config.settings import get_settings


@lru_cache
def get_engine():
    settings = get_settings()
    return create_engine(settings.get_db_url, echo=True)

@lru_cache
def get_session_local():
    return sessionmaker(autocommit=False, autoflush=False, bind=get_engine())


Base = declarative_base()
