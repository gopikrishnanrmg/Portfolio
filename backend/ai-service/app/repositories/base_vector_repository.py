from abc import ABC, abstractmethod

class BaseVectorRepository(ABC):

    @abstractmethod
    def get(self):
        pass

    @abstractmethod
    def reset(self):
        pass

    @abstractmethod
    def delete_all(self):
        pass

    @abstractmethod
    def initialize(self, text, metadata=None):
        pass

    @abstractmethod
    def rebuild(self, texts, metas):
        pass
